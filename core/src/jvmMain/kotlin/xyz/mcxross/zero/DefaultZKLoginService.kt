package xyz.mcxross.zero

import kotlinx.serialization.Serializable
import xyz.mcxross.zero.model.ZKLoginRequest

@Serializable
actual open class DefaultZKLoginService : ZKLoginService {
  override fun zkLogin(zkLoginRequest: ZKLoginRequest): Any {
    TODO("Not yet implemented")
  }
}
