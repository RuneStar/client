@file:JvmName("Resources")

package com.runesuite.client.common

import java.nio.file.Path
import java.nio.file.Paths

@JvmField
val GROUP_ID = "com.runesuite.client"

@JvmField
val ROOT_DIR_PATH: Path = Paths.get(System.getProperty("user.home"), "RuneSuite")

@JvmField
val CLIENT_ARTIFACT_ID = "client"

@JvmField
val CLIENT_PATH: Path = ROOT_DIR_PATH.resolve("$CLIENT_ARTIFACT_ID.jar")

@JvmField
val PLUGINS_JARS_DIR_PATH: Path = ROOT_DIR_PATH.resolve("plugins-jars")

@JvmField
val PLUGINS_STANDARD_ARTIFACT_ID = "client-plugins-standard"

@JvmField
val PLUGINS_STANDARD_PATH: Path = PLUGINS_JARS_DIR_PATH.resolve("$PLUGINS_STANDARD_ARTIFACT_ID.jar")

@JvmField
val PLUGINS_DIR_PATH: Path = ROOT_DIR_PATH.resolve("plugins")