@file:JvmName("Updater")

package org.runestar.client.updater

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.runestar.client.updater.common.ClassHook
import java.lang.invoke.MethodHandles
import java.net.URL

// ClassLoader that loaded this class
private val classLoader = MethodHandles.lookup().lookupClass().classLoader

@JvmField
val HOOKS: List<ClassHook> = jacksonObjectMapper().readValue(classLoader.getResource("hooks.json"))

/**
 * Original untouched gamepack.
 */
@JvmField
val GAMEPACK: URL = classLoader.getResource("gamepack.jar")

/**
 * Deobfuscated gamepack.
 */
@JvmField
val GAMEPACK_DEOB: URL = classLoader.getResource("gamepack.deob.jar")

@JvmField
val GAMEPACK_DEOB_RENAMED: URL = classLoader.getResource("gamepack.deob.renamed.jar")

/**
 * Gamepack with the META-INF directory removed, all enclosing method attributes removed, deobfuscated but keeps unused
 * methods and fields.
 */
@JvmField
val GAMEPACK_CLEAN: URL = classLoader.getResource("gamepack.clean.jar")