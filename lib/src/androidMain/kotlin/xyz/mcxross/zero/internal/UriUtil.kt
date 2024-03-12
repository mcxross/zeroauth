/*
 * Copyright 2016 McXross. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.mcxross.zero.internal

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.browser.customtabs.CustomTabsService
import androidx.core.util.Pair
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

/** Utility methods for extracting parameters from Uri objects. */
object UriUtil {
  fun parseUriIfAvailable(uri: String?): Uri? {
    return if (uri == null) {
      null
    } else Uri.parse(uri)
  }

  fun appendQueryParameterIfNotNull(uriBuilder: Uri.Builder, paramName: String, value: Any?) {
    if (value == null) {
      return
    }
    val valueStr = value.toString()
    uriBuilder.appendQueryParameter(paramName, value.toString())
  }

  fun getLongQueryParameter(uri: Uri, param: String): Long? {
    val valueStr = uri.getQueryParameter(param)
    return valueStr?.toLong()
  }

  fun toCustomTabUriBundle(uris: Array<out Uri?>?, startIndex: Int): List<Bundle> {
    require(startIndex >= 0) { "startIndex must be positive" }
    if (uris == null || uris.size <= startIndex) {
      return emptyList()
    }
    val uriBundles: MutableList<Bundle> = ArrayList(uris.size - startIndex)
    for (i in startIndex ..< uris.size) {
      if (uris[i] == null) {
        Logger.warn("Null URI in possibleUris list - ignoring")
        continue
      }
      val uriBundle = Bundle()
      uriBundle.putParcelable(CustomTabsService.KEY_URL, uris[i])
      uriBundles.add(uriBundle)
    }
    return uriBundles
  }

  fun formUrlEncode(parameters: Map<String, String>?): String {
    if (parameters == null) {
      return ""
    }
    val queryParts: MutableList<String?> = ArrayList()
    for ((key, value) in parameters) {
      queryParts.add(key + "=" + formUrlEncodeValue(value))
    }
    return TextUtils.join("&", queryParts)
  }

  fun formUrlEncodeValue(value: String): String {
    return try {
      URLEncoder.encode(value, "utf-8")
    } catch (ex: UnsupportedEncodingException) {
      // utf-8 should always be supported
      throw IllegalStateException("Unable to encode using UTF-8")
    }
  }

  fun formUrlDecode(encoded: String): List<Pair<String, String>> {
    if (TextUtils.isEmpty(encoded)) {
      return emptyList()
    }
    val parts = encoded.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val params: MutableList<Pair<String, String>> = ArrayList()
    for (part in parts) {
      val paramAndValue = part.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      val param = paramAndValue[0]
      val encodedValue = paramAndValue[1]
      try {
        params.add(Pair.create(param, URLDecoder.decode(encodedValue, "utf-8")))
      } catch (ex: UnsupportedEncodingException) {
        Logger.error("Unable to decode parameter, ignoring", ex)
      }
    }
    return params
  }

  fun formUrlDecodeUnique(encoded: String): Map<String?, String?> {
    val params = formUrlDecode(encoded)
    val uniqueParams: MutableMap<String?, String?> = HashMap()
    for (param in params) {
      uniqueParams[param.first] = param.second
    }
    return uniqueParams
  }
}
