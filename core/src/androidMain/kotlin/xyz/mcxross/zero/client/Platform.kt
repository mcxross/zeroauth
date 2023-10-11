package xyz.mcxross.zero.client

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual val defaultEngine: HttpClientEngine = OkHttp.create()