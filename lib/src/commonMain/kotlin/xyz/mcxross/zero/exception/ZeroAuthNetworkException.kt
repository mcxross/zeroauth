package xyz.mcxross.zero.exception

class ZeroAuthNetworkException : Exception {
  constructor(message: String) : super(message)

  constructor(message: String, cause: Throwable) : super(message, cause)
}
