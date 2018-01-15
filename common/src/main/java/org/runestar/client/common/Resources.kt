@file:JvmName("Resources")

package org.runestar.client.common

import java.awt.image.BufferedImage
import java.lang.invoke.MethodHandles
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

private val classLoader = MethodHandles.lookup().lookupClass().classLoader

@JvmField
val ICON: BufferedImage = ImageIO.read(classLoader.getResource("icon.png"))

const val GROUP_ID = "org.runestar.client"

const val TITLE = "RuneStar"

@JvmField
val ROOT_DIR_PATH: Path = Paths.get(System.getProperty("user.home"), "RuneStar")

const val CLIENT_ARTIFACT_ID = "client"

@JvmField
val CLIENT_PATH: Path = ROOT_DIR_PATH.resolve("$CLIENT_ARTIFACT_ID.jar")

@JvmField
val PROFILES_DIR_PATH: Path = ROOT_DIR_PATH.resolve("profiles")

const val PLUGINS_STANDARD_ARTIFACT_ID = "client-plugins-standard"

@JvmField
val PLUGINS_DIR_PATH: Path = ROOT_DIR_PATH.resolve("plugins")

@JvmField
val PLUGINS_STANDARD_PATH: Path = PLUGINS_DIR_PATH.resolve("$PLUGINS_STANDARD_ARTIFACT_ID.jar")

@JvmField
val CLIENT_PRM_PATH: Path = ROOT_DIR_PATH.resolve("client.prm")