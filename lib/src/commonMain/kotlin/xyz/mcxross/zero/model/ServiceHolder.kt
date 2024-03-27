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
package xyz.mcxross.zero.model

import xyz.mcxross.zero.service.ProvingService
import xyz.mcxross.zero.service.SaltingService

/**
 * Holds references to the salting and proving services used throughout the SDK.
 *
 * `ServiceHolder` acts as a centralized registry for services that are essential for the operation
 * of the SDK. It is crucial to initialize the `saltingService` and `provingService` before
 * performing any SDK operations.
 *
 * The SDK provides default implementations for these services, but they can be overridden with
 * custom implementations for flexibility. This design allows for easy swapping of service
 * implementations and ensures that the rest of the SDK operates with the correct service instances.
 *
 * Properties:
 * - [saltingService] : [SaltingService]? - Holds a reference to the salting service instance.
 *   Default is `null`, and must be initialized before any SDK operation.
 * - [provingService] : [ProvingService]? - Holds a reference to the proving service instance.
 *   Default is `null`, and must be initialized before any SDK operation.
 *
 * Usage:
 * ```
 * ServiceHolder.saltingService = CustomSaltingService()
 * ServiceHolder.provingService = CustomProvingService()
 * // Now the SDK will use the custom services for all subsequent operations.
 * ```
 */
object ServiceHolder {
  var saltingService: SaltingService? = null
  var provingService: ProvingService? = null
}
