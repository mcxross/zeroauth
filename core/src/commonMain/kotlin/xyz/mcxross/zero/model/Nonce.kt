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
package xyz.mcxross.zero.model

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import xyz.mcxross.zero.Constant.DEFAULT_MAX_EPOCH
import xyz.mcxross.zero.rpc.epoch
import xyz.mcxross.zero.serializer.DynamicSerializer
import xyz.mcxross.zero.util.generateNonce
import xyz.mcxross.zero.util.generateRandomness
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
sealed class Nonce {

  abstract fun generate(endPoint: String, callback: (String) -> Unit)

  @JsName("FromPubKey")
  @Serializable
  data class FromPubKey(
    @Serializable(with = DynamicSerializer::class) val pubKey: Any,
    val maximumEpoch: Int = DEFAULT_MAX_EPOCH,
    val randomness: String = generateRandomness()
  ) : Nonce() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun generate(endPoint: String, callback: (String) -> Unit) {
      GlobalScope.launch {
        val epoch = epoch(endPoint)
        callback(generateNonce(pubKey, epoch.toInt() + maximumEpoch, randomness))
      }
    }
  }

  @JsName("FromSecretKey")
  @Serializable
  data class FromSecretKey(
    val maximumEpoch: Int,
    val secretKey: String,
    val scheme: String,
    val randomness: String
  ) : Nonce() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun generate(endPoint: String, callback: (String) -> Unit) {
      GlobalScope.launch {
        val epoch = epoch(endPoint)
        callback(epoch.toString())
      }
    }
  }
}
