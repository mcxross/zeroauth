package xyz.mcxross.zero.model

import com.eygraber.uri.Uri
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationServiceConfiguration(
  /** The authorization service's endpoint. */
  val authorizationEndpoint: Uri,
  /** The authorization service's token exchange and refresh endpoint. */
  val tokenEndpoint: Uri,
  /** The end session service's endpoint; */
  val revocationEndpoint: Uri? = null,
  /** The authorization service's client registration endpoint. */
  val registrationEndpoint: Uri? = null,
  val discoveryDoc: AuthorizationServiceDiscovery? = null
)
