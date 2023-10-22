package xyz.mcxross.zero.model

import com.eygraber.uri.Uri
import kotlinx.serialization.Serializable
import xyz.mcxross.zero.serializer.UriSerializer

@Serializable
data class AuthorizationServiceConfiguration(
  /** The authorization service's endpoint. */
  @Serializable(with = UriSerializer::class) val authorizationEndpoint: Uri,
  /** The authorization service's token exchange and refresh endpoint. */
  @Serializable(with = UriSerializer::class) val tokenEndpoint: Uri,
  /** The end session service's endpoint; */
  @Serializable(with = UriSerializer::class) val revocationEndpoint: Uri? = null,
  /** The authorization service's client registration endpoint. */
  @Serializable(with = UriSerializer::class) val registrationEndpoint: Uri? = null,
  val discoveryDoc: AuthorizationServiceDiscovery? = null
)
