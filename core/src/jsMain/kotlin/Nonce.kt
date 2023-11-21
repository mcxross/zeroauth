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

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import xyz.mcxross.zero.rpc.epoch

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class MaximumEpoch(val value: Int) {
  fun toInternal(): xyz.mcxross.zero.model.MaximumEpoch {
    return xyz.mcxross.zero.model.MaximumEpoch(value)
  }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("EphemeralPublicKey")
@Serializable
data class EphemeralPublicKey(val value: String) {
  fun toInternal(): xyz.mcxross.zero.model.EphemeralPublicKey {
    return xyz.mcxross.zero.model.EphemeralPublicKey(value)
  }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Randomness")
@Serializable
data class Randomness(val value: String) {
  fun toInternal(): xyz.mcxross.zero.model.Randomness {
    return xyz.mcxross.zero.model.Randomness(value)
  }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Nonce")
@Serializable
sealed class Nonce {

  fun toInternal(): xyz.mcxross.zero.model.Nonce {
    return when (this) {
      is FromString -> xyz.mcxross.zero.model.Nonce.FromString(value)
      is FromComponents ->
        xyz.mcxross.zero.model.Nonce.FromComponents(
          maximumEpoch.toInternal(),
          ephemeralPublicKey.toInternal(),
          randomness.toInternal()
        )
    }
  }

  @JsName("FromString")
  @Serializable
  data class FromString(val value: String) : Nonce() {
    override fun toString(): String {
      return value
    }
  }

  @JsName("FromComponents")
  @Serializable
  data class FromComponents(
    val maximumEpoch: MaximumEpoch,
    val ephemeralPublicKey: EphemeralPublicKey,
    val randomness: Randomness
  ) : Nonce() {
    fun generate(string: String, callback: (String) -> Unit) {
      GlobalScope.launch {
        val epoch = epoch(string)
        callback(epoch.toString())
      }
    }
  }
}
