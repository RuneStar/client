@file:JvmName("Resources")

package com.runesuite.client.common

import java.nio.file.Path
import java.nio.file.Paths

@JvmField
val ROOT_DIR_PATH: Path = Paths.get(System.getProperty("user.home"), "RuneSuite")

@JvmField
val CLIENT_PATH: Path = ROOT_DIR_PATH.resolve("client.jar")

@JvmField
val PLUGINS_DIR_PATH: Path = ROOT_DIR_PATH.resolve("plugins")

@JvmField
val PLUGINS_PATH: Path = PLUGINS_DIR_PATH.resolve("runesuite-plugins.jar")

@JvmField
val PLUGINS_SETTINGS_DIR_PATH: Path = ROOT_DIR_PATH.resolve("plugins-settings")