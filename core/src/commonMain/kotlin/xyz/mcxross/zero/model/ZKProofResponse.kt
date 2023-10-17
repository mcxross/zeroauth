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

import kotlinx.serialization.Serializable

interface ZKProofResponse

@Serializable
data class ProofPoints(val a: List<String>, val b: List<List<String>>, val c: List<String>)

@Serializable data class IssBase64Details(val value: String, val indexMod4: Int)

@Serializable
data class DefaultZKProofResponse(
  val proofPoints: ProofPoints,
  val issBase64Details: IssBase64Details,
  val headerBase64: String
) : ZKProofResponse
