@file:JvmName("Updater")

package org.runestar.client.updater

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.runestar.client.updater.common.ClassHook
import java.lang.invoke.MethodHandles

// ClassLoader that loaded this class
private val classLoader = MethodHandles.lookup().lookupClass().classLoader

@JvmField
val HOOKS: List<ClassHook> = jacksonObjectMapper().readValue(classLoader.getResource("hooks.json"))