@file:JvmName("Resources")

package org.runestar.client.api

import java.awt.image.BufferedImage
import java.lang.invoke.MethodHandles
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * 16x16 RuneStar star icon
 */
@JvmField
val ICON: BufferedImage = ImageIO.read(MethodHandles.lookup().lookupClass().getResource("icon.png"))

const val TITLE = "RuneStar"

@JvmField
val ROOT_DIR_PATH: Path = Paths.get(System.getProperty("user.home"), "RuneStar")

@JvmField
val PROFILES_DIR_PATH: Path = ROOT_DIR_PATH.resolve("profiles")