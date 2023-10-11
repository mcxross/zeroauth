package xyz.mcxross.zero.util

import xyz.mcxross.zero.model.Provider

fun url(provider: Provider, params: Map<String, String>): String {
  val baseUrl = provider.url()
  val query = params.map { "${it.key}=${it.value}" }.joinToString("&")
  return "$baseUrl?$query"
}
