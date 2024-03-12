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
package xyz.mcxross.zero.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class DefaultClient : Client {
  val client = HttpClient(defaultEngine) { install(ContentNegotiation) { json() } }

  suspend inline fun <reified T, reified U> request(
      url: String,
      request: T,
  ): U {
    val response: U =
        client
            .post(url) {
              contentType(ContentType.Application.Json)
              setBody(request)
            }
            .body()
    return response
  }

  suspend inline fun <reified T> submitForm(
      url: String,
      formParameters: Map<String, String>,
  ): T {
    val response: T =
        client
            .submitForm(
                url = url,
                formParameters =
                    parameters { formParameters.forEach { (key, value) -> append(key, value) } })
            .body()
    return response
  }
}
