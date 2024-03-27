@file:JvmName("ExtensionAndroid")
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
package xyz.mcxross.zero.extension

import android.content.Context
import android.content.Intent
import kotlin.jvm.Throws
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import xyz.mcxross.zero.exception.ZeroAuthNetworkException
import xyz.mcxross.zero.model.AuthorizationManagementResponse
import xyz.mcxross.zero.model.AuthorizationResponse
import xyz.mcxross.zero.model.AuthorizationResponse.Companion.EXTRA_RESPONSE
import xyz.mcxross.zero.model.ZKLoginRequest
import xyz.mcxross.zero.model.ZKLoginResponse

fun AuthorizationResponse.Companion.fromIntent(dataIntent: Intent): AuthorizationResponse? {
  val jsonStr = dataIntent.getStringExtra(EXTRA_RESPONSE)
  return jsonStr?.let { Json.decodeFromString(serializer(), it) }
}

/**
 * Produces an intent containing this authorization response. This is used to deliver the
 * authorization response to the registered handler after a call to {@link
 * AuthorizationService#performAuthorizationRequest}.
 */
fun AuthorizationManagementResponse.toIntent(): Intent =
    Intent().apply { putExtra(EXTRA_RESPONSE, Json.encodeToString(serializer(), this@toIntent)) }

@Throws(ZeroAuthNetworkException::class)
suspend infix fun Context.zkLogin(request: ZKLoginRequest): Intent =
    xyz.mcxross.zero.zkLogin(this, request)

fun ZKLoginResponse.toIntent(): Intent =
    Intent().apply { putExtra(EXTRA_RESPONSE, Json.encodeToString(serializer(), this@toIntent)) }

fun ZKLoginResponse.fromJson(jsonStr: String): ZKLoginResponse? =
    Json.decodeFromString(serializer(), jsonStr)
