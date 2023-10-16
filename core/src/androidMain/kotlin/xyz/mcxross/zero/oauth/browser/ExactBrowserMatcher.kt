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

/** Matches only the specified browser. */
class ExactBrowserMatcher
/** Creates a browser matcher that will only match the specified browser. */
(private val mBrowser: BrowserDescriptor) : BrowserMatcher {
  override fun matches(descriptor: BrowserDescriptor): Boolean {
    return mBrowser == descriptor
  }
}
