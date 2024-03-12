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
package xyz.mcxross.zero.oauth.browser

/**
 * A denyList of browsers. This will reject a match for any browser on the list, and permit all
 * others. Examples:
 * ```java
 * // denyList Chrome, whether using a custom tab or not
 * new BrowserDenyList(
 * VersionedBrowserMatcher.CHROME_BROWSER,
 * VersionedBrowserMatcher.CHROME_CUSTOM_TAB);
 *
 *
 * // denyList Firefox
 * new BrowserDenyList(
 * VersionedBrowserMatcher.FIREFOX_BROWSER,
 * VersionedBrowserMatcher.FIREFOX_CUSTOM_TAB);
 *
 *
 * // denyList Dolphin Browser
 * new BrowserDenyList(
 * new VersionedBrowserMatcher(
 * "mobi.mgeek.TunnyBrowser",
 * "<DOLPHIN_SIGNATURE>",
 * false,
 * VersionRange.ANY_VERSION));
 * }
 * ```
 *
 * </DOLPHIN_SIGNATURE>
 */
class BrowserDenyList(vararg matchers: BrowserMatcher) : BrowserMatcher {
  private val mBrowserMatchers: MutableList<BrowserMatcher>

  /** Creates a denyList from the provided set of matchers. */
  init {
    mBrowserMatchers = mutableListOf(*matchers)
  }

  override fun matches(descriptor: BrowserDescriptor): Boolean {
    for (matcher in mBrowserMatchers) {
      if (matcher.matches(descriptor)) {
        return false
      }
    }
    return true
  }
}
