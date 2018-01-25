@file:JvmName("System")

package org.runestar.client.common

import java.awt.SystemColor.info

private val systemProperties = listOf(
        "java.version",
        "java.vm.version",
        "java.vm.vendor",
        "java.home",
        "os.name",
        "os.version",
        "user.dir",
        "java.class.path"
)

private val debugSystemProperties = systemProperties.associate { it to System.getProperty(it) }

val systemDebugString = "System: properties=$debugSystemProperties"