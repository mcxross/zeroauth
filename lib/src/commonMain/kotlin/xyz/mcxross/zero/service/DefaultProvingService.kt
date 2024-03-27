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
package xyz.mcxross.zero.service

import kotlinx.coroutines.flow.flow
import xyz.mcxross.zero.client.DefaultClient
import xyz.mcxross.zero.model.FlowProofResponse
import xyz.mcxross.zero.model.ProvingResponseWrapper
import xyz.mcxross.zero.model.ZKLoginRequest

class DefaultProvingService(override var endPoint: String) : ProvingService {

  private val client = DefaultClient(endPoint)

  override fun prove(
      salt: String,
      input: String,
      zkLoginRequest: ZKLoginRequest?
  ): ProvingResponseWrapper =
      FlowProofResponse(
          flow {
            val response = client.makeRequest(salt, input, zkLoginRequest)
            // emit(response)
          })
}
