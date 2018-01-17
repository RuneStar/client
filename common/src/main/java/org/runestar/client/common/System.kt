@file:JvmName("System")

package org.runestar.client.common

private val systemProperties = listOf(
        "java.version",
        "java.vm.version",
        "java.vm.vendor",
        "java.home",
        "os.name",
        "os.version"
)

val debugSystemProperties = systemProperties.associate { it to System.getProperty(it) }