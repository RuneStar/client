@file:JvmName("Resources")

package com.runesuite.client.common

import java.nio.file.Path
import java.nio.file.Paths

const val GROUP_ID = "com.runesuite.client"

const val TITLE = "RuneSuite"

@JvmField
val ROOT_DIR_PATH: Path = Paths.get(System.getProperty("user.home"), "RuneSuite")

const val CLIENT_ARTIFACT_ID = "client"

@JvmField
val CLIENT_PATH: Path = ROOT_DIR_PATH.resolve("$CLIENT_ARTIFACT_ID.jar")

@JvmField
val PLUGINS_JARS_DIR_PATH: Path = ROOT_DIR_PATH.resolve("plugins-jars")

const val PLUGINS_STANDARD_ARTIFACT_ID = "client-plugins-standard"

@JvmField
val PLUGINS_STANDARD_PATH: Path = PLUGINS_JARS_DIR_PATH.resolve("$PLUGINS_STANDARD_ARTIFACT_ID.jar")

@JvmField
val PLUGINS_DIR_PATH: Path = ROOT_DIR_PATH.resolve("plugins")