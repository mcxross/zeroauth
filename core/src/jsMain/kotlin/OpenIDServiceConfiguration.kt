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

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("OpenIDServiceConfiguration")
@Serializable
data class OpenIDServiceConfiguration(
  val provider: Provider,
  val clientId: String,
  val redirectUri: String,
  val nonce: Nonce = Nonce.FromPubKey(Ed25519Keypair().getPublicKey()),
) {
  internal fun toInternal(): xyz.mcxross.zero.model.OpenIDServiceConfiguration {
    return xyz.mcxross.zero.model.OpenIDServiceConfiguration(
      provider.toInternal(),
      clientId,
      redirectUri,
      nonce.toInternal()
    )
  }
}
