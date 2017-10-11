@file:JvmName("Updater")

package com.runesuite.client.updater

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.runesuite.client.updater.common.ClassHook
import java.net.URL

@JvmField
val HOOKS: List<ClassHook> = jacksonObjectMapper().readValue(::HOOKS.javaClass.classLoader.getResource("hooks.json"))

@JvmField
val GAMEPACK: URL = ::GAMEPACK.javaClass.classLoader.getResource("gamepack.jar")

@JvmField
val GAMEPACK_DEOB: URL = ::GAMEPACK_DEOB.javaClass.classLoader.getResource("gamepack.deob.jar")