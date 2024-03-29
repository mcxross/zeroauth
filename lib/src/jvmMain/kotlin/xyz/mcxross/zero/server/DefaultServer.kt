/*
 * Copyright 2016 McXross. All Rights Reserved.
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.mcxross.zero.server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import xyz.mcxross.zero.feedback.callback.ZKLoginFeedback
import xyz.mcxross.zero.model.Option

class DefaultServer(
    private val zkLoginFeedback: ZKLoginFeedback? = null,
    private val port: Int = 9000,
    redirectPath: String = "/redirect"
) : Server {

  private var server: ApplicationEngine? = null

  private val formattedRedirectPath = redirectPath.takeIf { it.startsWith("/") } ?: "/$redirectPath"

  override fun start() {

    server =
        embeddedServer(Netty, port = port) {
              routing {
                get(formattedRedirectPath) {
                  call.respondText("Authorized!")
                  val code = call.request.queryParameters["id_token"]
                  zkLoginFeedback?.onToken(if (code == null) Option.None else Option.Some(code))
                }
              }
            }
            .start(wait = true)
  }

  override fun stop() {}
}
