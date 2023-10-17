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
package xyz.mcxross.zero.zk

import xyz.mcxross.zero.client.DefaultClient
import xyz.mcxross.zero.model.ProvingServiceConfiguration
import xyz.mcxross.zero.model.ZKProofResponse

object ProvingService {
  private val provingClient = DefaultClient()

  suspend fun request(provingServiceConfiguration: ProvingServiceConfiguration): ZKProofResponse {
    return provingClient.request(
      provingServiceConfiguration.url,
      provingServiceConfiguration.request,
    )
  }
}
