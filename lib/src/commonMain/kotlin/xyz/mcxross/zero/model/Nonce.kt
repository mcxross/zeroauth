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
import xyz.mcxross.suiness.deriveNewKey
import xyz.mcxross.suiness.generateNonce
import xyz.mcxross.suiness.generateRandomness
import xyz.mcxross.zero.Constant.DEFAULT_MAX_EPOCH
import xyz.mcxross.zero.extension.lift
import xyz.mcxross.zero.rpc.epoch
import xyz.mcxross.zero.serializer.KeyDetailsSerializer

@Serializable
sealed class Nonce {
  abstract fun generate(callback: (String) -> Unit)

  abstract suspend fun generateAsync(): String

  @Serializable
  data class FromComponents(
      val endPoint: String = "https://fullnode.devnet.sui.io",
      @Serializable(with = KeyDetailsSerializer::class) val kp: KeyDetails = newKey(),
      val maxEpoch: Int = DEFAULT_MAX_EPOCH,
      val randomness: String = generateRandomness()
  ) : Nonce() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun generate(callback: (String) -> Unit) {
      GlobalScope.launch {
        val epoch = epoch(endPoint)
        callback(generateNonce(kp.sk, (epoch.toInt() + maxEpoch).toULong(), randomness))
      }
    }

    override suspend fun generateAsync(): String {
      val epoch = epoch(endPoint)
      return generateNonce(kp.sk, (epoch.toInt() + maxEpoch).toULong(), randomness)
    }
  }

  @Serializable
  data class FromStr(val value: String) : Nonce() {
    override fun generate(callback: (String) -> Unit) {
      callback(value)
    }

    override suspend fun generateAsync(): String {
      return value
    }
  }
}

private fun newKey(): KeyDetails {
  return deriveNewKey().lift()
}

// @OptIn(ExperimentalJsExport::class)
/*@Serializable
sealed class Nonce {

  abstract fun generate(endPoint: String, callback: (String) -> Unit)

  abstract suspend fun generateAsync(endPoint: String): String

  @JsName("FromPubKey")
  @Serializable
  data class FromPubKey(
      val endPoint: String = "https://fullnode.devnet.sui.io",
      @Serializable(with = DynamicSerializer::class) val pk: Any = generateKey(),
      val maximumEpoch: Int = DEFAULT_MAX_EPOCH,
      val randomness: String = generateRandomness()
  ) : Nonce() {

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

    override suspend fun generateAsync(endPoint: String): String {
      TODO("Not yet implemented")
    }
  }
}*/
