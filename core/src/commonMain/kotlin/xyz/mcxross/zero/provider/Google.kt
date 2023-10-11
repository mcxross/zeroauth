package xyz.mcxross.zero.provider

expect class Google : Provider {
  override fun prompt(): String?
}
