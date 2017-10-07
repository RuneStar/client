@file:JvmName("Resources")

package com.runesuite.client.common

import java.nio.file.Paths

val ROOT_DIR_PATH = Paths.get(System.getProperty("user.home"), "RuneSuite")

val CLIENT_PATH = ROOT_DIR_PATH.resolve("client.jar")

val PLUGINS_DIR_PATH = ROOT_DIR_PATH.resolve("plugins")

val PLUGINS_PATH = PLUGINS_DIR_PATH.resolve("runesuite-plugins.jar")

val PLUGINS_SETTINGS_DIR_PATH = ROOT_DIR_PATH.resolve("plugins-settings")