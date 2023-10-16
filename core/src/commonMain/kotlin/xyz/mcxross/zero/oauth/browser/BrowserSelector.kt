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
package xyz.mcxross.zero.oauth.browser

expect object BrowserSelector {

  /**
   * Selects a browser to use for authorization.
   *
   * @param browserMatcher The [BrowserMatcher] to use when selecting a browser to use for
   *   authorization.
   * @param browsers The list of browsers to select from.
   * @return The [BrowserDescriptor] to use for authorization, or `null` if no browser was selected.
   */
  fun select(browserMatcher: BrowserMatcher, browsers: List<BrowserDescriptor>): BrowserDescriptor?
}
