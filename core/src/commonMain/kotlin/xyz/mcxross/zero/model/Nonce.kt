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

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class) @JsExport @Serializable data class MaximumEpoch(val value: Int)

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class EphemeralPublicKey(val value: String)

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Randomness(val value: String)

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
sealed class Nonce {
  @Serializable
  data class FromString(val value: String) : Nonce() {
    override fun toString(): String {
      return value
    }
  }

  @Serializable
  data class FromComponents(
    val maximumEpoch: MaximumEpoch,
    val ephemeralPublicKey: EphemeralPublicKey,
    val randomness: Randomness
  ) : Nonce()
}
