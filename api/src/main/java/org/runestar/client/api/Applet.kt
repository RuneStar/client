@file:Suppress("DEPRECATION")

package org.runestar.client.api

import org.runestar.general.JavConfig
import java.applet.Applet
import java.awt.Color

internal fun buildApplet(
        applet: Applet,
        javConfig: JavConfig
) {
    applet.apply {
        layout = null
        setStub(JavConfig.AppletStub(javConfig))
        maximumSize = javConfig.appletMaxSize
        preferredSize = javConfig.appletMinSize
        minimumSize = preferredSize
        background = Color.BLACK
        size = preferredSize
    }
}