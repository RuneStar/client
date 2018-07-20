@file:Suppress("DEPRECATION")

package org.runestar.client.api

import org.runestar.client.common.JAV_CONFIG
import org.runestar.client.common.JavConfig
import java.applet.Applet
import java.awt.Color

internal fun buildApplet(
        applet: Applet
) {
    val javConfig = JAV_CONFIG
    applet.apply {
        layout = null
        setStub(JavConfig.AppletStub(javConfig))
        maximumSize = javConfig.appletMaxSize
        preferredSize = javConfig.appletMinSize
        minimumSize = preferredSize
        background = Color.BLACK
        foreground = Color.BLACK
        size = preferredSize
    }
}