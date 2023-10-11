package xyz.mcxross.zero.server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

actual class DefaultServer : Server {
  actual override fun start() {
    embeddedServer(Netty, port = 8080) { routing { get("/") { call.respondText("zero server") } } }
      .start(wait = true)
  }

  actual override fun stop() {
    TODO()
  }
}
