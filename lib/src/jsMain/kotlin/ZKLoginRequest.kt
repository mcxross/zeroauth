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
import kotlinx.serialization.Serializable
import xyz.mcxross.zero.Mysten
import xyz.mcxross.zero.service.DefaultProvingService
import xyz.mcxross.zero.service.DefaultSaltingService
import xyz.mcxross.zero.service.ProvingService
import xyz.mcxross.zero.service.SaltingService

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("ZKLoginRequest")
@Serializable
data class ZKLoginRequest(
  val openIDServiceConfiguration: OpenIDServiceConfiguration,
  val fullNode: String = "https://fullnode.devnet.sui.io",
  val saltingService: SaltingService =
    DefaultSaltingService(Mysten.MYSTEN_LABS_SALTING_SERVICE_URL),
  val provingService: ProvingService = DefaultProvingService(Mysten.MYSTEN_LABS_PROVING_SERVICE_URL)
) {
  fun toInternal(): xyz.mcxross.zero.model.ZKLoginRequest {
    return xyz.mcxross.zero.model.ZKLoginRequest(
      openIDServiceConfiguration.toInternal(),
      fullNode,
      saltingService,
      provingService
    )
  }
}
