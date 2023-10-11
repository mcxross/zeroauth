package xyz.mcxross.zero.provider


actual class Google : Provider {
  actual override fun prompt(): String? {

    return "Hello from Android!"
  }
}
