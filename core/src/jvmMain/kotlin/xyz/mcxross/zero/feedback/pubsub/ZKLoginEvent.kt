package xyz.mcxross.zero.feedback.pubsub

sealed class ZKLoginEvent {
    class TokenSuccess(val result: String) : ZKLoginEvent()
    class TokenFailure(val error: Exception) : ZKLoginEvent()
}

interface ZKLoginEventListener {
    fun onServerEvent(event: ZKLoginEvent)
}
