@file:JvmName("Common")

package org.runestar.client.common

@JvmField
val JAV_CONFIG: JavConfig = JavConfig.load(System.getProperty("runestar.world", ""))

@JvmField
val REVISION: Int = lookupClassLoader.getResource("revision.txt").readText().toInt()

const val MANIFEST_NAME = "gamepack.MANIFEST.MF"

const val DIFF_NAME = "gamepack.diff"