package xyz.mcxross.zero.provider

actual class Google : Provider {
  actual override fun prompt(): String? {
    throw NotImplementedError("An operation is not implemented: Not implemented yet!")
  }
}
