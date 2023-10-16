/*
 * Copyright 2016 McXross. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.mcxross.zero.oauth

import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import xyz.mcxross.zero.exception.AuthorizationException
import xyz.mcxross.zero.extension.toIntent
import xyz.mcxross.zero.extension.toZeroUri
import xyz.mcxross.zero.internal.Logger
import xyz.mcxross.zero.model.AuthorizationManagementRequest
import xyz.mcxross.zero.model.AuthorizationManagementResponse

/**
 * Stores state and handles events related to the authorization management flow. The activity is
 * started by [DefaultAuthorizationService.performAuthorizationRequest] or
 * [DefaultAuthorizationService.performEndSessionRequest], and records all state pertinent to the
 * authorization management request before invoking the authorization intent. It also functions to
 * control the back stack, ensuring that the authorization activity will not be reachable via the
 * back button after the flow completes.
 *
 * The following diagram illustrates the operation of the activity:
 * ```
 * Back Stack Towards Top
 * +------------------------------------------>
 *
 * +------------+            +---------------+      +----------------+      +--------------+
 * |            |     (1)    |               | (2)  |                | (S1) |              |
 * | Initiating +----------->| Authorization +----->| Authorization  +----->| Redirect URI |
 * |  Activity  |            |  Management   |      |   Activity     |      |   Receiver   |
 * |            |<-----------+   Activity    |<-----+ (e.g. browser) |      |   Activity   |
 * |            | (C2b, S3b) |               | (C1) |                |      |              |
 * +------------+            +-+---+---------+      +----------------+      +-------+------+
 * |  |  ^                                              |
 * |  |  |                                              |
 * +-------+  |  |                      (S2)                    |
 * |          |  +----------------------------------------------+
 * |          |
 * |          v (S3a)
 * (C2a) |      +------------+
 * |      |            |
 * |      | Completion |
 * |      |  Activity  |
 * |      |            |
 * |      +------------+
 * |
 * |      +-------------+
 * |      |             |
 * +----->| Cancelation |
 * |  Activity   |
 * |             |
 * +-------------+
 * ```
 *
 * The process begins with an activity requesting that an authorization flow be started, using
 * [AuthorizationService.performAuthorizationRequest] or
 * [AuthorizationService.performEndSessionRequest].
 * - Step 1: Using an intent derived from [.createStartIntent], this activity is started. The state
 *   delivered in this intent is recorded for future use.
 * - Step 2: The authorization intent, typically a browser tab, is started. At this point, depending
 *   on user action, we will either end up in a "completion" flow (S) or "cancelation flow" (C).
 * - Cancelation (C) flow:
 * - Step C1: If the user presses the back button or otherwise causes the authorization activity to
 *   finish, the AuthorizationManagementActivity will be recreated or restarted.
 * - Step C2a: If a cancellation PendingIntent was provided in the call to
 *   [DefaultAuthorizationService.performAuthorizationRequest] or
 *   [DefaultAuthorizationService.performEndSessionRequest], then this is used to invoke a
 *   cancelation activity.
 * - Step C2b: If no cancellation PendingIntent was provided (legacy behavior, or
 *   AuthorizationManagementActivity was started with an intent from
 *   [DefaultAuthorizationService.getAuthorizationRequestIntent] or
 *
 * @link DefaultAuthorizationService#performEndOfSessionRequest}), then the
 *   AuthorizationManagementActivity simply finishes after calling [Activity.setResult], with
 *   [Activity.RESULT_CANCELED], returning control to the activity above it in the back stack
 *   (typically, the initiating activity).
 * - Completion (S) flow:
 * - Step S1: The authorization activity completes with a success or failure, and sends this result
 *   to [RedirectUriReceiverActivity].
 * - Step S2: [RedirectUriReceiverActivity] extracts the forwarded data, and invokes
 *   AuthorizationManagementActivity using an intent derived from [.createResponseHandlingIntent].
 *   This intent has flag CLEAR_TOP set, which will result in both the authorization activity and
 *   [RedirectUriReceiverActivity] being destroyed, if necessary, such that
 *   AuthorizationManagementActivity is once again at the top of the back stack.
 * - Step S3a: If this activity was invoked via
 *   [DefaultAuthorizationService.performAuthorizationRequest] or
 *   [DefaultAuthorizationService.performEndSessionRequest], then the pending intent provided for
 *   completion of the authorization flow is invoked, providing the decoded
 *   [AuthorizationManagementResponse] or [AuthorizationException] as appropriate. The
 *   AuthorizationManagementActivity finishes, removing itself from the back stack.
 * - Step S3b: If this activity was invoked via an intent returned by
 *   [DefaultAuthorizationService.getAuthorizationRequestIntent], then this activity calls
 *   [Activity.setResult] with [Activity.RESULT_OK] and a data intent containing the
 *   [AuthorizationResponse] or [AuthorizationException] as appropriate. The
 *   AuthorizationManagementActivity finishes, removing itself from the back stack.
 */
class AuthorizationManagementActivity : AppCompatActivity() {
  private var authorizationStarted = false
  private var authIntent: Intent? = null
  private var authRequest: AuthorizationManagementRequest? = null
  private var completeIntent: PendingIntent? = null
  private var cancelIntent: PendingIntent? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      extractState(intent.extras)
    } else {
      extractState(savedInstanceState)
    }
  }

  override fun onResume() {
    super.onResume()

    /*
     * If this is the first run of the activity, start the authorization intent.
     * Note that we do not finish the activity at this point, in order to remain on the back
     * stack underneath the authorization activity.
     */
    if (!authorizationStarted) {
      try {
        startActivity(authIntent)
        authorizationStarted = true
      } catch (e: ActivityNotFoundException) {
        handleBrowserNotFound()
        finish()
      }
      return
    }

    /*
     * On a subsequent run, it must be determined whether we have returned to this activity
     * due to an OAuth2 redirect, or the user canceling the authorization flow. This can
     * be done by checking whether a response URI is available, which would be provided by
     * RedirectUriReceiverActivity. If it is not, we have returned here due to the user
     * pressing the back button, or the authorization activity finishing without
     * RedirectUriReceiverActivity having been invoked - this can occur when the user presses
     * the back button, or closes the browser tab.
     */
    if (intent.data != null) {
      handleAuthorizationComplete()
    } else {
      handleAuthorizationCanceled()
    }
    finish()
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putBoolean(KEY_AUTHORIZATION_STARTED, authorizationStarted)
    outState.putParcelable(KEY_AUTH_INTENT, authIntent)
    outState.putString(KEY_AUTH_REQUEST, Json.encodeToString(serializer(), authRequest))
    outState.putString(
      KEY_AUTH_REQUEST_TYPE,
      AuthorizationManagementUtil.requestTypeFor(authRequest)
    )
    outState.putParcelable(KEY_COMPLETE_INTENT, completeIntent)
    outState.putParcelable(KEY_CANCEL_INTENT, cancelIntent)
  }

  private fun handleAuthorizationComplete() {
    val responseUri: Uri? = intent.data
    val responseData: Intent = extractResponseData(responseUri)
    if (responseData == null) {
      Logger.error("Failed to extract OAuth2 response from redirect")
      return
    }
    responseData.setData(responseUri)
    sendResult(completeIntent, responseData, RESULT_OK)
  }

  private fun handleAuthorizationCanceled() {
    Logger.debug("Authorization flow canceled by user")
    val cancelData: Intent =
      AuthorizationException.fromTemplate(
          AuthorizationException.GeneralErrors.USER_CANCELED_AUTH_FLOW,
          null
        )
        .toIntent()
    sendResult(cancelIntent, cancelData, RESULT_CANCELED)
  }

  private fun handleBrowserNotFound() {
    Logger.debug("Authorization flow canceled due to missing browser")
    val cancelData: Intent =
      AuthorizationException.fromTemplate(
          AuthorizationException.GeneralErrors.PROGRAM_CANCELED_AUTH_FLOW,
          null
        )
        .toIntent()
    sendResult(cancelIntent, cancelData, RESULT_CANCELED)
  }

  @Suppress("DEPRECATION")
  private fun extractState(state: Bundle?) {
    if (state == null) {
      Logger.warn("No stored state - unable to handle response")
      finish()
      return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      authIntent = state.getParcelable(KEY_AUTH_INTENT, Intent::class.java)
      authorizationStarted = state.getBoolean(KEY_AUTHORIZATION_STARTED, false)
      completeIntent = state.getParcelable(KEY_COMPLETE_INTENT, PendingIntent::class.java)
      cancelIntent = state.getParcelable(KEY_CANCEL_INTENT, PendingIntent::class.java)
    } else {
      authIntent = state.getParcelable(KEY_AUTH_INTENT)
      authorizationStarted = state.getBoolean(KEY_AUTHORIZATION_STARTED, false)
      completeIntent = state.getParcelable(KEY_COMPLETE_INTENT)
      cancelIntent = state.getParcelable(KEY_CANCEL_INTENT)
    }

    try {
      val authRequestJson = state.getString(KEY_AUTH_REQUEST, null)
      val authRequestType = state.getString(KEY_AUTH_REQUEST_TYPE, null)
      authRequest =
        if (authRequestJson != null)
          AuthorizationManagementUtil.requestFrom(authRequestJson, authRequestType)
        else null
    } catch (ex: Exception) {
      sendResult(
        cancelIntent,
        AuthorizationException.AuthorizationRequestErrors.INVALID_REQUEST.toIntent(),
        RESULT_CANCELED
      )
    }
  }

  private fun sendResult(callback: PendingIntent?, cancelData: Intent, resultCode: Int) {
    if (callback != null) {
      try {
        callback.send(this, 0, cancelData)
      } catch (e: PendingIntent.CanceledException) {
        Logger.error("Failed to send cancel intent", e)
      }
    } else {
      setResult(resultCode, cancelData)
    }
  }

  private fun extractResponseData(responseUri: Uri?): Intent {
    return if (
      responseUri!!.getQueryParameterNames().contains(AuthorizationException.PARAM_ERROR)
    ) {
      AuthorizationException.fromOAuthRedirect(responseUri.toZeroUri()).toIntent()
    } else {
      val response: AuthorizationManagementResponse =
        AuthorizationManagementUtil.responseWith(authRequest, responseUri.toZeroUri())
      if (
        authRequest!!.state == null && response.state != null ||
          authRequest!!.state != null && authRequest!!.state != response.state
      ) {
        Logger.warn(
          "State returned in authorization response (%s) does not match state " +
            "from request (%s) - discarding response",
          response.state,
          authRequest!!.state
        )
        return AuthorizationException.AuthorizationRequestErrors.STATE_MISMATCH.toIntent()
      }
      response.toIntent()
    }
  }

  companion object {
    @VisibleForTesting val KEY_AUTH_INTENT = "authIntent"

    @VisibleForTesting val KEY_AUTH_REQUEST = "authRequest"

    @VisibleForTesting val KEY_AUTH_REQUEST_TYPE = "authRequestType"

    @VisibleForTesting val KEY_COMPLETE_INTENT = "completeIntent"

    @VisibleForTesting val KEY_CANCEL_INTENT = "cancelIntent"

    @VisibleForTesting val KEY_AUTHORIZATION_STARTED = "authStarted"

    /**
     * Creates an intent to start an authorization flow.
     *
     * @param context the package context for the app.
     * @param request the authorization request which is to be sent.
     * @param authIntent the intent to be used to get authorization from the user.
     * @param completeIntent the intent to be sent when the flow completes.
     * @param cancelIntent the intent to be sent when the flow is canceled.
     */
    fun createStartIntent(
      context: Context,
      request: AuthorizationManagementRequest,
      authIntent: Intent?,
      completeIntent: PendingIntent?,
      cancelIntent: PendingIntent?
    ): Intent {
      val intent: Intent = createBaseIntent(context)
      intent.putExtra(KEY_AUTH_INTENT, authIntent)
      intent.putExtra(KEY_AUTH_REQUEST, Json.encodeToString(serializer(), request))
      intent.putExtra(KEY_AUTH_REQUEST_TYPE, AuthorizationManagementUtil.requestTypeFor(request))
      intent.putExtra(KEY_COMPLETE_INTENT, completeIntent)
      intent.putExtra(KEY_CANCEL_INTENT, cancelIntent)
      return intent
    }

    /**
     * Creates an intent to start an authorization flow.
     *
     * @param context the package context for the app.
     * @param request the authorization management request which is to be sent.
     * @param authIntent the intent to be used to get authorization from the user.
     */
    fun createStartForResultIntent(
      context: Context,
      request: AuthorizationManagementRequest,
      authIntent: Intent?
    ): Intent {
      return createStartIntent(context, request, authIntent, null, null)
    }

    /**
     * Creates an intent to handle the completion of an authorization flow. This restores the
     * original AuthorizationManagementActivity that was created at the start of the flow.
     *
     * @param context the package context for the app.
     * @param responseUri the response URI, which carries the parameters describing the response.
     */
    fun createResponseHandlingIntent(context: Context, responseUri: Uri?): Intent {
      val intent: Intent = createBaseIntent(context)
      intent.setData(responseUri)
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
      return intent
    }

    private fun createBaseIntent(context: Context): Intent {
      return Intent(context, AuthorizationManagementActivity::class.java)
    }
  }
}
