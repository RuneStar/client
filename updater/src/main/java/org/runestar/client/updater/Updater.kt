@file:JvmName("Updater")

package org.runestar.client.updater

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.runestar.client.updater.common.ClassHook
import java.lang.invoke.MethodHandles
import java.net.URL

val hooksFile: URL get() = MethodHandles.lookup().lookupClass().classLoader.getResource("hooks.json")

fun readHooks(): List<ClassHook> {
    return jacksonObjectMapper().readValue(hooksFile)
}