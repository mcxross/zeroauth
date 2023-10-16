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
 * Matches a browser based on its package name, set of signatures, version and whether it is being
 * used as a custom tab. This can be used as part of a browser allowList or denyList.
 */
class VersionedBrowserMatcher
/**
 * Creates a browser matcher that requires an exact match on package name, set of signature hashes,
 * custom tab usage mode, and a version range.
 */
(
  private val mPackageName: String,
  private val mSignatureHashes: Set<String>,
  private val mUsingCustomTab: Boolean,
  private val mVersionRange: VersionRange
) : BrowserMatcher {
  /**
   * Creates a browser matcher that requires an exact match on package name, single signature hash,
   * custom tab usage mode, and a version range.
   */
  constructor(
    packageName: String?,
    signatureHash: String,
    usingCustomTab: Boolean,
    versionRange: VersionRange?
  ) : this(packageName!!, setOf<String>(signatureHash), usingCustomTab, versionRange!!)

  override fun matches(descriptor: BrowserDescriptor): Boolean {
    return mPackageName == descriptor.packageName &&
      mUsingCustomTab == descriptor.useCustomTab &&
      mVersionRange.matches(descriptor.version) &&
      mSignatureHashes == descriptor.signatureHashes
  }

  companion object {
    /** Matches any version of Chrome for use as a custom tab. */
    val CHROME_CUSTOM_TAB =
      VersionedBrowserMatcher(
        Browsers.Chrome.PACKAGE_NAME,
        Browsers.Chrome.SIGNATURE_SET,
        true,
        VersionRange.atLeast(Browsers.Chrome.MINIMUM_VERSION_FOR_CUSTOM_TAB)
      )

    /** Matches any version of Google Chrome for use as a standalone browser. */
    val CHROME_BROWSER =
      VersionedBrowserMatcher(
        Browsers.Chrome.PACKAGE_NAME,
        Browsers.Chrome.SIGNATURE_SET,
        false,
        VersionRange.ANY_VERSION
      )

    /** Matches any version of Firefox for use as a custom tab. */
    val FIREFOX_CUSTOM_TAB =
      VersionedBrowserMatcher(
        Browsers.Firefox.PACKAGE_NAME,
        Browsers.Firefox.SIGNATURE_SET,
        true,
        VersionRange.atLeast(Browsers.Firefox.MINIMUM_VERSION_FOR_CUSTOM_TAB)
      )

    /** Matches any version of Mozilla Firefox. */
    val FIREFOX_BROWSER =
      VersionedBrowserMatcher(
        Browsers.Firefox.PACKAGE_NAME,
        Browsers.Firefox.SIGNATURE_SET,
        false,
        VersionRange.ANY_VERSION
      )

    /** Matches any version of SBrowser for use as a standalone browser. */
    val SAMSUNG_BROWSER =
      VersionedBrowserMatcher(
        Browsers.SBrowser.PACKAGE_NAME,
        Browsers.SBrowser.SIGNATURE_SET,
        false,
        VersionRange.ANY_VERSION
      )

    /** Matches any version of SBrowser for use as a custom tab. */
    val SAMSUNG_CUSTOM_TAB =
      VersionedBrowserMatcher(
        Browsers.SBrowser.PACKAGE_NAME,
        Browsers.SBrowser.SIGNATURE_SET,
        true,
        VersionRange.atLeast(Browsers.SBrowser.MINIMUM_VERSION_FOR_CUSTOM_TAB)
      )
  }
}
