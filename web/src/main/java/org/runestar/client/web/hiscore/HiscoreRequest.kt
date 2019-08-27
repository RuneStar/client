package org.runestar.client.web.hiscore

import java.net.http.HttpRequest

data class HiscoreRequest private constructor(
        val playerName: String,
        val endpoint: HiscoreEndpoint
) {

    internal fun toHttpRequest() = HttpRequest.newBuilder(endpoint.query(playerName)).build()

    companion object {

        fun of(playerName: String, endpoint: HiscoreEndpoint) = HiscoreRequest(canonicalPlayerName(playerName), endpoint)

        private fun canonicalPlayerName(playerName: String) = playerName.toLowerCase()
                .replace(' ', '_')
                .replace('-', '_')
    }
}