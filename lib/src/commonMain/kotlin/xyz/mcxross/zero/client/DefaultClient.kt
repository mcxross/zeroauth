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
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import xyz.mcxross.suiness.getExtendedEphemeralPublicKey
import xyz.mcxross.zero.model.DefaultProofRequest
import xyz.mcxross.zero.model.Nonce
import xyz.mcxross.zero.model.Proof
import xyz.mcxross.zero.model.ZKLoginRequest

class DefaultClient(val url: String) : Client {
  val client = HttpClient(defaultEngine) { install(ContentNegotiation) { json() } }

  suspend fun makeRequest(salt: String, jwt: String, zkLoginRequest: ZKLoginRequest?): Proof {
    val response: HttpResponse =
        client.post("https://prover-dev.mystenlabs.com/v1") {
          contentType(ContentType.Application.Json)
          if (zkLoginRequest != null &&
              zkLoginRequest.openIDServiceConfiguration.nonce is Nonce.FromComponents) {
            setBody(
                DefaultProofRequest(
                    jwt,
                    getExtendedEphemeralPublicKey(
                        zkLoginRequest.openIDServiceConfiguration.nonce.kp.sk),
                    zkLoginRequest.openIDServiceConfiguration.nonce.maxEpoch,
                    zkLoginRequest.openIDServiceConfiguration.nonce.randomness,
                    salt))
          }
        }

    client.close()
    return Json.decodeFromString(serializer(), response.bodyAsText())
  }

  suspend inline fun <reified T, reified U> request(
      url: String,
      request: T,
  ): U? {
    var response: U? = null
    try {
      response =
          client
              .post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
              }
              .body()
    } catch (e: Exception) {
      println("sss $e")
    }
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
