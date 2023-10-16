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
package xyz.mcxross.zero.oauth.browser

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.annotation.WorkerThread
import androidx.browser.customtabs.*
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import xyz.mcxross.zero.internal.Logger
import xyz.mcxross.zero.internal.UriUtil

/**
 * Hides the details of establishing connections and sessions with custom tabs, to make testing
 * easier.
 */
class CustomTabManager(context: Context) {
  private val mContextRef: WeakReference<Context>
  private val mClient: AtomicReference<CustomTabsClient?>
  private val mClientLatch: CountDownLatch
  private var mConnection: CustomTabsServiceConnection? = null

  init {
    mContextRef = WeakReference(context)
    mClient = AtomicReference()
    mClientLatch = CountDownLatch(1)
  }

  @Synchronized
  fun bind(browserPackage: String) {
    if (mConnection != null) {
      return
    }
    mConnection =
      object : CustomTabsServiceConnection() {
        override fun onServiceDisconnected(componentName: ComponentName) {
          Logger.debug("CustomTabsService is disconnected")
          setClient(null)
        }

        override fun onCustomTabsServiceConnected(
          componentName: ComponentName,
          customTabsClient: CustomTabsClient
        ) {
          Logger.debug("CustomTabsService is connected")
          customTabsClient.warmup(0)
          setClient(customTabsClient)
        }

        private fun setClient(client: CustomTabsClient?) {
          mClient.set(client)
          mClientLatch.countDown()
        }
      }
    val context = mContextRef.get()
    if (
      context == null ||
        !CustomTabsClient.bindCustomTabsService(
          context,
          browserPackage,
          mConnection as CustomTabsServiceConnection
        )
    ) {
      // this is expected if the browser does not support custom tabs
      Logger.info("Unable to bind custom tabs service")
      mClientLatch.countDown()
    }
  }

  /**
   * Creates a [custom tab builder][androidx.browser.customtabs.CustomTabsIntent.Builder], with an
   * optional list of optional URIs that may be requested. The URI list should be ordered such that
   * the most likely URI to be requested is first. If the selected browser does not support custom
   * tabs, then the URI list has no effect.
   */
  @WorkerThread
  fun createTabBuilder(vararg possibleUris: Uri?): CustomTabsIntent.Builder {
    return CustomTabsIntent.Builder(createSession(null, *possibleUris))
  }

  @Synchronized
  fun dispose() {
    if (mConnection == null) {
      return
    }
    val context = mContextRef.get()
    context?.unbindService(mConnection!!)
    mClient.set(null)
    Logger.debug("CustomTabsService is disconnected")
  }

  /**
   * Creates a [custom tab session][androidx.browser.customtabs.CustomTabsSession] for use with a
   * custom tab intent, with optional callbacks and optional list of URIs that may be requested. The
   * URI list should be ordered such that the most likely URI to be requested is first. If no custom
   * tab supporting browser is available, this will return `null`.
   */
  @WorkerThread
  fun createSession(callbacks: CustomTabsCallback?, vararg possibleUris: Uri?): CustomTabsSession? {
    val client = client ?: return null
    val session = client.newSession(callbacks)
    if (session == null) {
      Logger.warn("Failed to create custom tabs session through custom tabs client")
      return null
    }
    if (possibleUris != null && possibleUris.isNotEmpty()) {
      val additionalUris: List<Bundle> = UriUtil.toCustomTabUriBundle(possibleUris, 1)
      session.mayLaunchUrl(possibleUris[0], null, additionalUris)
    }
    return session
  }

  @get:WorkerThread
  val client: CustomTabsClient?
    /**
     * Retrieve the custom tab client used to communicate with the custom tab supporting browser, if
     * available.
     */
    get() {
      try {
        mClientLatch.await(CLIENT_WAIT_TIME, TimeUnit.SECONDS)
      } catch (e: InterruptedException) {
        Logger.info("Interrupted while waiting for browser connection")
        mClientLatch.countDown()
      }
      return mClient.get()
    }

  companion object {
    /** Wait for at most this amount of time for the browser connection to be established. */
    private const val CLIENT_WAIT_TIME = 1L
  }
}
