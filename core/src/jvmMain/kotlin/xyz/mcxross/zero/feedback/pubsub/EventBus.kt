package xyz.mcxross.zero.feedback.pubsub

object EventBus {
  private val listeners = mutableListOf<ZKLoginEventListener>()

  fun subscribe(listener: ZKLoginEventListener) {
    listeners.add(listener)
  }

  fun unsubscribe(listener: ZKLoginEventListener) {
    listeners.remove(listener)
  }

  fun publish(event: ZKLoginEvent) {
    listeners.forEach { it.onServerEvent(event) }
  }
}
