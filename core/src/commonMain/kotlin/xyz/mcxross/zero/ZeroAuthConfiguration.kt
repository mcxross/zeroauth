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
package xyz.mcxross.zero

import xyz.mcxross.zero.oauth.browser.AnyBrowserMatcher
import xyz.mcxross.zero.oauth.browser.BrowserMatcher
import xyz.mcxross.zero.model.Provider

/**
 * Configuration for the ZeroAuth library.
 *
 * @param browserMatcher The [BrowserMatcher] to use when selecting a browser to use for
 *   authorization.
 * @param skipIssuerHttpsCheck Whether to skip the HTTPS check on the issuer URI.
 */
data class ZeroAuthConfiguration(
  val provider: Provider,
  val browserMatcher: BrowserMatcher = AnyBrowserMatcher.INSTANCE,
  val skipIssuerHttpsCheck: Boolean = false,
)
