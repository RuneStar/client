@file:JvmName("Common")

package org.runestar.client.common

import java.lang.invoke.MethodHandles

@JvmField
val JAV_CONFIG: JavConfig = JavConfig.load(System.getProperty("runestar.world", ""))

@JvmField
val REVISION: Int = MethodHandles.lookup().lookupClass().classLoader
        .getResource("revision.txt")
        .readText(Charsets.UTF_8).toInt()
