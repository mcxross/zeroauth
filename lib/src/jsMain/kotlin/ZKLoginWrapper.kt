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
@file:JsModule("@mysten/zklogin")
@file:JsNonModule
@JsName("generateRandomness") external fun generateRandomnessJs(): String

@JsName("generateNonce")
external fun generateNonceJs(pubKey: dynamic, maxEpoch: Int, randomness: String): String

@JsName("generateNonceByScheme")
external fun generateNonceJs(
  pubKey: dynamic,
  scheme: String,
  maxEpoch: Long,
  randomness: String
): String
