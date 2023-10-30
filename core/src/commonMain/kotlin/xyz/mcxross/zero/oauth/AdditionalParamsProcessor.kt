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
package xyz.mcxross.zero.oauth

import kotlinx.serialization.json.Json
import xyz.mcxross.zero.model.AdditionalParams

object AdditionalParamsProcessor {
  fun builtInParams(vararg params: String): Set<String> {
    if (params.isEmpty()) {
      return emptySet()
    }
    return params.toSet()
  }

  fun extractAdditionalParams(jsonString: String): Map<String, String> {
    val additionalParams = Json.decodeFromString<AdditionalParams>(jsonString)

    return mapOf(
      "key1" to additionalParams.key1.orEmpty(),
      "key2" to additionalParams.key2.orEmpty()
    )
  }
}
