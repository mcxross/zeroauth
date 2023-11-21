package xyz.mcxross.zero.rpc

import xyz.mcxross.ksui.client.EndPoint
import xyz.mcxross.ksui.client.suiHttpClient

suspend fun epoch(endPoint: EndPoint): Long {
  val suiHttpClient = suiHttpClient {
    endpoint = endPoint
    maxRetries = 10
  }

  return suiHttpClient.getLatestSuiSystemState().epoch
}

suspend fun epoch(endPoint: String): Long {
  val suiHttpClient = suiHttpClient {
    endpoint = EndPoint.CUSTOM
    customEndPointUrl = endPoint
    maxRetries = 10
  }

  return suiHttpClient.getLatestSuiSystemState().epoch
}
