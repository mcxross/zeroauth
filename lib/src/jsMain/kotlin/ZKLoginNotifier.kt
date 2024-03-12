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
import xyz.mcxross.zero.model.ZKLoginRequest
import xyz.mcxross.zero.model.ZKLoginResponse

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("ZKLoginNotifier")
class ZKLoginNotifier {
  private var listener: ZKLoginListener? = null

  fun setListener(listener: ZKLoginListener) {
    this.listener = listener
  }

  /** The zkLogin complete callback. */
  fun onZKLoginComplete(request: ZKLoginRequest, response: ZKLoginResponse?, error: ZKLoginError?) {
    listener?.invoke(request, response, error)
  }
}
