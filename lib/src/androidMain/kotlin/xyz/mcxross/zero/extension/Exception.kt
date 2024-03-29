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

import android.content.Intent
import android.net.Uri
import kotlinx.serialization.json.Json
import xyz.mcxross.zero.exception.ZeroAuthAuthorizationException
import xyz.mcxross.zero.exception.ZeroAuthAuthorizationException.Companion.EXTRA_EXCEPTION
import xyz.mcxross.zero.exception.ZeroAuthAuthorizationException.Companion.PARAM_ERROR
import xyz.mcxross.zero.exception.ZeroAuthAuthorizationException.Companion.PARAM_ERROR_DESCRIPTION
import xyz.mcxross.zero.exception.ZeroAuthAuthorizationException.Companion.PARAM_ERROR_URI
import xyz.mcxross.zero.exception.ZeroAuthAuthorizationException.Companion.fromJsonString

fun ZeroAuthAuthorizationException.toIntent(): Intent {
  val data = Intent()
  data.putExtra(
      EXTRA_EXCEPTION, Json.encodeToString(ZeroAuthAuthorizationException.serializer(), this))
  return data
}

fun ZeroAuthAuthorizationException.fromIntent(data: Intent): ZeroAuthAuthorizationException? {
  if (data.hasExtra(EXTRA_EXCEPTION)) {
    val jsonStr = data.getStringExtra(EXTRA_EXCEPTION)
    return fromJsonString(jsonStr ?: "")
  }
  return null
}

fun ZeroAuthAuthorizationException.fromOAuthRedirect(
    redirectUri: Uri
): ZeroAuthAuthorizationException {
  val error = redirectUri.getQueryParameter(PARAM_ERROR)
  val errorDescription = redirectUri.getQueryParameter(PARAM_ERROR_DESCRIPTION)
  val errorUri = redirectUri.getQueryParameter(PARAM_ERROR_URI)
  val (type, code, _, errorDescription1, errorUri1) =
      ZeroAuthAuthorizationException.AuthorizationRequestErrors.byString(error)
  return ZeroAuthAuthorizationException(
      type,
      code,
      error,
      errorDescription ?: errorDescription1,
      ((if (errorUri != null) Uri.parse(errorUri) else errorUri1) as com.eygraber.uri.Uri?),
      null)
}
