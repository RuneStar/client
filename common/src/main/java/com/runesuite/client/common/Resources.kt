@file:JvmName("Resources")

package com.runesuite.client.common

import java.nio.file.Paths

val ROOT_DIR = Paths.get(System.getProperty("user.home"), "RuneSuite")

val CORE = ROOT_DIR.resolve("core.jar")

val PLUGINS_DIR = ROOT_DIR.resolve("plugins")

val PLUGINS = PLUGINS_DIR.resolve("runesuite-plugins.jar")

val PLUGINS_SETTINGS_DIR = ROOT_DIR.resolve("plugins-settings")