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

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import xyz.mcxross.zero.model.Apple
import xyz.mcxross.zero.model.Facebook
import xyz.mcxross.zero.model.Ghost
import xyz.mcxross.zero.model.Google
import xyz.mcxross.zero.model.Provider
import xyz.mcxross.zero.model.Slack
import xyz.mcxross.zero.model.Twitch

expect val zeroSerializationModule: SerializersModule

val commonSerializersModule = SerializersModule {
  polymorphic(Provider::class) {
    subclass(Google::class)
    subclass(Facebook::class)
    subclass(Ghost::class)
    subclass(Twitch::class)
    subclass(Apple::class)
    subclass(Slack::class)
  }
}

object Zero {
  private val serializers = mutableListOf(zeroSerializationModule, commonSerializersModule)

  val json =
    kotlinx.serialization.json.Json {
      serializersModule = serializers.reduce { acc, serializersModule -> acc + serializersModule }
    }

  operator fun plusAssign(serializersModule: SerializersModule) {
    serializers.add(serializersModule)
  }
}
