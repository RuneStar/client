package org.runestar.client.web.hiscore

import org.runestar.client.web.bodyHandler
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture

internal class HiscoreClient(private val httpClient: HttpClient) {

    operator fun get(request: HiscoreRequest): CompletableFuture<HiscoreResult?> {
        return httpClient.sendAsync(request.toHttpRequest(), bodyHandler(HttpResponse.BodySubscribers.ofLines(StandardCharsets.US_ASCII)))
                .thenApply { it.body()?.let { HiscoreResult.of(it) } }
    }
}