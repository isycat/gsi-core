package com.isycat.dotahalp.gsi

import kotlinx.serialization.Serializable

interface GsiGameState {
    val auth: GsiAuth?
    val provider: GsiProvider?
}

@Serializable
data class GsiAuth(
    val token: String
)

@Serializable
data class GsiProvider(
    val name: String,
    val appid: Int,
    val version: Int,
    val timestamp: Long
) {
    val friendlyName: String get() = "$name ($appid)" // don't serialize
}
