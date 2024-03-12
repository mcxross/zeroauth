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
import xyz.mcxross.zero.model.LiveDataProofResponse
import xyz.mcxross.zero.model.ProofRequest
import xyz.mcxross.zero.model.ProofResponse
import xyz.mcxross.zero.model.ProvingResponseWrapper
import xyz.mcxross.zero.model.ProvingService

class AndroidDefaultProvingService(override var endPoint: String) : ProvingService {

  var lifecycleOwner: LifecycleOwner? = null
  private val client = DefaultClient()

  override fun prove(input: ProofRequest): ProvingResponseWrapper {
    val liveData = MutableLiveData<ProofResponse?>()
    lifecycleOwner?.lifecycleScope?.launch(Dispatchers.IO) {
      val response = client.request<ProofRequest, ProofResponse>(endPoint, input)
      withContext(Dispatchers.Main) { liveData.value = response }
    }
    return LiveDataProofResponse(liveData)
  }
}
