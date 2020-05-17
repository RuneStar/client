package org.runestar.client.api.web

import java.io.IOException
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.time.Duration
import java.util.concurrent.Executors

internal val httpClient = HttpClient.newBuilder()
        .executor(Executors.newSingleThreadExecutor())
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(5))
        .build()

internal fun <T> bodyHandler(onSuccess: HttpResponse.BodySubscriber<T?>) = HttpResponse.BodyHandler<T?> { responseInfo ->
    when(responseInfo.statusCode()) {
        200 -> onSuccess
        404 -> HttpResponse.BodySubscribers.replacing(null)
        else -> throw IOException("Invalid response status code: ${responseInfo.statusCode()}")
    }
}