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
package xyz.mcxross.zero.login

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.mcxross.zero.client.DefaultClient
import xyz.mcxross.zero.model.LiveDataSaltResponse
import xyz.mcxross.zero.model.SaltRequest
import xyz.mcxross.zero.model.SaltResponse
import xyz.mcxross.zero.model.SaltResponseWrapper
import xyz.mcxross.zero.model.SaltingService

class AndroidDefaultSaltingService(override var endPoint: String) : SaltingService {

  var lifecycleOwner: LifecycleOwner? = null
  private val client = DefaultClient()

  override fun salt(input: SaltRequest): SaltResponseWrapper {
    val liveData = MutableLiveData<SaltResponse?>()
    lifecycleOwner?.lifecycleScope?.launch(Dispatchers.IO) {
      val response = client.request<String, SaltResponse>(endPoint, input.jwtToken)
      withContext(Dispatchers.Main) { liveData.value = response }
    }
    return LiveDataSaltResponse(liveData)
  }
}
