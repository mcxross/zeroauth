package xyz.mcxross.zero.server

expect class DefaultServer : Server {
  override fun start()

  override fun stop()
}
