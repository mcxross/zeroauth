package xyz.mcxross.zero.client

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

actual val defaultEngine: HttpClientEngine = Js.create()
