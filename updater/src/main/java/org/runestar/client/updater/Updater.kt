@file:JvmName("Updater")

package org.runestar.client.updater

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.runestar.client.common.lookupClassLoader
import org.runestar.client.updater.common.ClassHook
import java.net.URL

val hooksFile: URL get() = lookupClassLoader.getResource("hooks.json")

fun readHooks(): List<ClassHook> = jacksonObjectMapper().readValue(hooksFile)

val gamepackFile: URL get() = lookupClassLoader.getResource("gamepack.jar")

val cleanGamepackFile: URL get() = lookupClassLoader.getResource("gamepack.clean.jar")