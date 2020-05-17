package org.runestar.client.api.web.hiscore

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.runestar.client.api.web.httpClient
import java.time.Duration
import java.util.concurrent.CompletableFuture

object Hiscores {

    private val client = HiscoreClient(httpClient)

    private val cache: LoadingCache<HiscoreRequest, CompletableFuture<HiscoreResult?>> = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofHours(1))
            .build(object : CacheLoader<HiscoreRequest, CompletableFuture<HiscoreResult?>>() {
                override fun load(key: HiscoreRequest) = client[key]
            })

    fun lookup(request: HiscoreRequest): CompletableFuture<HiscoreResult?> = cache[request]
}