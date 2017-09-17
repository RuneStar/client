package com.runesuite.client.updater

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.runesuite.client.updater.common.ClassHook
import java.io.InputStream

val HOOKS: List<ClassHook> by lazy {
    val jsonUrl = object {}.javaClass.classLoader.getResource("hooks.json")
    jacksonObjectMapper().readValue<List<ClassHook>>(jsonUrl)
}

fun getGamepack(): InputStream {
    return object {}.javaClass.classLoader.getResourceAsStream("gamepack.deob.jar")
}