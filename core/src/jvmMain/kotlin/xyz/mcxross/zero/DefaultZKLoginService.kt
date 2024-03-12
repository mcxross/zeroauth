package xyz.mcxross.zero

import com.eygraber.uri.toURI
import java.awt.Desktop
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.Serializable
import xyz.mcxross.zero.extension.toAuthorizationRequest
import xyz.mcxross.zero.feedback.callback.ZKLoginFeedback
import xyz.mcxross.zero.model.ZKLoginRequest
import xyz.mcxross.zero.model.toUri
import xyz.mcxross.zero.server.DefaultServer

fun zkLogin(zkLoginRequest: ZKLoginRequest, zkLoginFeedback: ZKLoginFeedback? = null) =
    runBlocking {
      val serverTimeoutMillis = 5 * 60 * 1000L

      val server =
          DefaultServer(
              zkLoginFeedback = zkLoginFeedback,
          )

      val serverJob = launch {
        try {
          withTimeout(serverTimeoutMillis) { server.start() }
        } catch (e: Exception) {
          // Handle the timeout here...
        }
      }

      if (Desktop.isDesktopSupported()) {
        val desktop = Desktop.getDesktop()
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
          zkLoginRequest.toAuthorizationRequest { desktop.browse(it.toUri().toURI()) }
        }
      }

      // Wait for the server coroutine to complete
      serverJob.join()
    }

@Serializable
actual open class DefaultZKLoginService : ZKLoginService {
  override fun zkLogin(zkLoginRequest: ZKLoginRequest): Any =
      xyz.mcxross.zero.zkLogin(zkLoginRequest)
}
