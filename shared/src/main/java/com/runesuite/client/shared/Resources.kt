@file:JvmName("Resources")

package com.runesuite.client.shared

import java.nio.file.Paths

val ROOT_DIR = Paths.get(System.getProperty("user.home"), "RuneSuite")

val GAMEPACK = ROOT_DIR.resolve("gamepack.jar")

val CORE = ROOT_DIR.resolve("core.jar")

val PLUGINS_DIR = ROOT_DIR.resolve("plugins")

val PLUGINS_SETTINGS_DIR = ROOT_DIR.resolve("plugins-settings")