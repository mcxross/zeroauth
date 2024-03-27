package xyz.mcxross.zero.model

import kotlinx.serialization.Serializable

@Serializable data class KeyDetails(val address: String, val sk: String, val phrase: String)
