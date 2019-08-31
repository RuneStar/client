package org.runestar.client.web.ge

import org.runestar.client.web.bodyHandler
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

internal class GeGraphClient(private val httpClient: HttpClient) {

    operator fun get(id: Int): CompletableFuture<GeGraphResult?> {
        return httpClient.sendAsync(GET(id), bodyHandler(HttpResponse.BodySubscribers.ofInputStream()))
                .thenApply { it.body()?.let { GeGraphResult.of(it) } }
    }

    private companion object {

        fun url(id: Int) = URI("https://services.runescape.com/m=itemdb_oldschool/api/graph/$id.json")

        fun GET(id: Int) = HttpRequest.newBuilder(url(id)).build()
    }
}