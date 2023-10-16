package xyz.mcxross.zero.oauth

import android.app.Activity
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import xyz.mcxross.zero.ZeroAuthConfiguration
import xyz.mcxross.zero.extension.toAndroidUri
import xyz.mcxross.zero.internal.Logger
import xyz.mcxross.zero.model.AuthorizationManagementRequest
import xyz.mcxross.zero.model.AuthorizationRequest
import xyz.mcxross.zero.model.EndSessionRequest
import xyz.mcxross.zero.oauth.browser.BrowserDescriptor
import xyz.mcxross.zero.oauth.browser.BrowserSelector
import xyz.mcxross.zero.oauth.browser.CustomTabManager

actual class DefaultAuthorizationService(
  val context: Context,
  val zeroAuthConfiguration: ZeroAuthConfiguration,
  val customTabManager: CustomTabManager = CustomTabManager(context),
  val browserDescriptor: BrowserDescriptor? =
    BrowserSelector.select(context, zeroAuthConfiguration.browserMatcher),
  private var disposable: Boolean = false
) : AuthorizationService {

  /**
   * Creates a custom tab builder.
   *
   * The builder that will use a tab session from an existing connection to a web browser, if
   * available.
   *
   * @param possibleUris The URIs that may be used to warm up the browser.
   * @return The [CustomTabsIntent.Builder] that will use a tab session from an existing connection
   */
  private fun createCustomTabsIntentBuilder(vararg possibleUris: Uri?): CustomTabsIntent.Builder {
    checkNotDisposed()
    return customTabManager.createTabBuilder(*possibleUris)
  }

  /**
   * Initiates an authorization request.
   *
   * This method initiates an authorization request, optionally using a provided CustomTabsIntent.
   * The method performs an authorization management request using the provided parameters and
   * either the provided CustomTabsIntent or a default one if none is provided.
   *
   * @param request The [AuthorizationRequest] containing the authorization details.
   * @param completedIntent (Optional) The [PendingIntent] to be triggered upon successful
   *   authorization.
   * @param canceledIntent (Optional) The [PendingIntent] to be triggered if authorization is
   *   canceled.
   * @param customTabsIntent (Optional) The [CustomTabsIntent] used for the authorization request.
   *   If null, a default one will be created and used.
   */
  fun performAuthorizationRequest(
    request: AuthorizationRequest,
    completedIntent: PendingIntent? = null,
    canceledIntent: PendingIntent? = null,
    customTabsIntent: CustomTabsIntent? = null
  ) =
    performAuthManagementRequest(
      request,
      completedIntent,
      canceledIntent,
      customTabsIntent ?: createCustomTabsIntentBuilder().build()
    )

  /**
   * Sends an end session request to the authorization service.
   *
   * It will use a [custom tab](https://developer.chrome.com/multidevice/android/customtabs) if
   * available, or a browser instance. The parameters of this request are determined by both the
   * authorization service configuration and the provided [request object][EndSessionRequest]. Upon
   * completion of this request, the provided [completion PendingIntent][PendingIntent] will be
   * invoked. If the user cancels the authorization request, the current activity will regain
   * control.
   *
   * @param request The [EndSessionRequest] containing the end session details.
   * @param completedIntent The [PendingIntent] to be triggered upon successful end session.
   * @param canceledIntent (Optional) The [PendingIntent] to be triggered if end session is
   *   canceled.
   * @param customTabsIntent (Optional) The [CustomTabsIntent] used for the end session request. If
   *   null, a default one will be created and used.
   */
  fun performEndSessionRequest(
    request: EndSessionRequest,
    completedIntent: PendingIntent,
    canceledIntent: PendingIntent? = null,
    customTabsIntent: CustomTabsIntent = createCustomTabsIntentBuilder().build()
  ) = performAuthManagementRequest(request, completedIntent, canceledIntent, customTabsIntent)

  private fun performAuthManagementRequest(
    request: AuthorizationManagementRequest,
    completedIntent: PendingIntent?,
    canceledIntent: PendingIntent?,
    customTabsIntent: CustomTabsIntent
  ) {
    val authIntent: Intent = prepareAuthorizationRequestIntent(request, customTabsIntent)
    val startIntent: Intent =
      AuthorizationManagementActivity.createStartIntent(
        context,
        request,
        authIntent,
        completedIntent,
        canceledIntent
      )

    // Calling start activity from outside an activity requires FLAG_ACTIVITY_NEW_TASK.
    if (!isActivity(context)) {
      startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(startIntent)
  }

  /**
   * Determines whether the provided context is associated with an Activity.
   *
   * This function checks if the given [Context] is an instance of [Activity]. If the provided
   * context is a [ContextWrapper], the function will traverse up the context chain to determine
   * whether any parent context is an Activity. This is useful for ensuring that operations
   * requiring an Activity context (such as starting an activity) are only performed in appropriate
   * contexts.
   *
   * @param context The [Context] to be checked for its association with an Activity.
   * @return [Boolean] Returns `true` if the provided context is an Activity or is wrapped by an
   *   Activity context. Returns `false` otherwise.
   */
  private fun isActivity(context: Context): Boolean {
    var localContext: Context = context
    while (localContext is ContextWrapper) {
      if (localContext is Activity) {
        return true
      }
      localContext = (context as ContextWrapper).baseContext
    }
    return false
  }

  /**
   * Constructs an intent that encapsulates the provided request and an optionally provided custom
   * tabs intent.
   *
   * It is intended to be launched via [Activity.startActivityForResult]. The parameters of this
   * request are determined by both the authorization service configuration and the provided
   * [request object][AuthorizationRequest]. Upon completion of this request, the activity that gets
   * launched will call [Activity.setResult] with [Activity.RESULT_OK] and an [Intent] containing
   * authorization completion information. If the user presses the back button or closes the browser
   * tab, the launched activity will call [Activity.setResult] with [Activity.RESULT_CANCELED]
   * without a data [Intent]. Note that [Activity.RESULT_OK] indicates the authorization request
   * completed, not necessarily that it was a successful authorization.
   *
   * @param request The [AuthorizationRequest] containing the authorization details.
   * @param customTabsIntent The intent that will be used to start the custom tab. If not provided,
   *   a default one will be created. It is recommended that this intent be created with the help of
   *   [.createCustomTabsIntentBuilder], which will ensure that a warmed-up version of the browser
   *   will be used, minimizing latency.
   * @throws android.content.ActivityNotFoundException if no suitable browser is available to
   *   perform the authorization flow.
   */
  fun getAuthorizationRequestIntent(
    request: AuthorizationRequest,
    customTabsIntent: CustomTabsIntent = createCustomTabsIntentBuilder().build()
  ): Intent {
    val authIntent: Intent = prepareAuthorizationRequestIntent(request, customTabsIntent)
    return AuthorizationManagementActivity.createStartForResultIntent(context, request, authIntent)
  }

  /**
   * Constructs an intent that encapsulates the provided request and custom tabs intent, and is
   * intended to be launched via [Activity.startActivityForResult]. The parameters of this request
   * are determined by both the authorization service configuration and the provided
   * [request object][AuthorizationRequest]. Upon completion of this request, the activity that gets
   * launched will call [Activity.setResult] with [Activity.RESULT_OK] and an [Intent] containing
   * authorization completion information. If the user presses the back button or closes the browser
   * tab, the launched activity will call [Activity.setResult] with [Activity.RESULT_CANCELED]
   * without a data [Intent]. Note that [Activity.RESULT_OK] indicates the authorization request
   * completed, not necessarily that it was a successful authorization.
   *
   * @param customTabsIntent The intent that will be used to start the custom tab. It is recommended
   *   that this intent be created with the help of [.createCustomTabsIntentBuilder], which will
   *   ensure that a warmed-up version of the browser will be used, minimizing latency.
   * @throws android.content.ActivityNotFoundException if no suitable browser is available to
   *   perform the authorization flow.
   */
  fun getEndSessionRequestIntent(
    request: EndSessionRequest,
    customTabsIntent: CustomTabsIntent = createCustomTabsIntentBuilder().build()
  ): Intent {
    val authIntent: Intent = prepareAuthorizationRequestIntent(request, customTabsIntent)
    return AuthorizationManagementActivity.createStartForResultIntent(context, request, authIntent)
  }

  private fun prepareAuthorizationRequestIntent(
    request: AuthorizationManagementRequest,
    customTabsIntent: CustomTabsIntent
  ): Intent {
    checkNotDisposed()
    if (browserDescriptor == null) {
      throw ActivityNotFoundException()
    }

    val requestUri: Uri = request.toUri().toAndroidUri()
    val intent: Intent =
      if (browserDescriptor.useCustomTab) {
        customTabsIntent.intent
      } else {
        Intent(Intent.ACTION_VIEW)
      }
    intent.setPackage(browserDescriptor.packageName)
    intent.setData(requestUri)
    Logger.debug(
      "Using %s as browser for auth, custom tab = %s",
      intent.getPackage()!!,
      browserDescriptor.useCustomTab.toString()
    )

    // TODO fix logger for configuration
    // Logger.debug("Initiating authorization request to %s"
    // request.configuration.authorizationEndpoint);
    return intent
  }

  /**
   * Disposes state that will not normally be handled by garbage collection.
   *
   * This should be called when the authorization service is no longer required, including when any
   * owning activity is paused or destroyed (i.e. in [android.app.Activity.onStop]).
   */
  fun dispose() {
    if (disposable) {
      return
    }
    customTabManager.dispose()
    disposable = true
  }

  /**
   * Checks whether the service has been disposed.
   *
   * @throws IllegalStateException if the service has been disposed.
   */
  private fun checkNotDisposed() {
    check(!disposable) { "Service has been disposed and rendered inoperable" }
  }
}
