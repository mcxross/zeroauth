package xyz.mcxross.zero.provider

interface AsyncProvider {
    suspend fun prompt(): String?
}