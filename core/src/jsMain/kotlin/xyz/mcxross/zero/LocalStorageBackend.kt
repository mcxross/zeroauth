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

import kotlinx.browser.window
import org.w3c.dom.Storage

abstract class StorageBackend {
  abstract suspend fun getItem(name: String): String?

  abstract suspend fun removeItem(name: String)

  abstract suspend fun clear()

  abstract suspend fun setItem(name: String, value: String)
}

class LocalStorageBackend(private val storage: Storage = window.localStorage) : StorageBackend() {
  override suspend fun getItem(name: String): String? {
    return storage.getItem(name)
  }

  override suspend fun removeItem(name: String) {
    storage.removeItem(name)
  }

  override suspend fun clear() {
    storage.clear()
  }

  override suspend fun setItem(name: String, value: String) {
    storage.setItem(name, value)
  }
}
