@file:Suppress("DEPRECATION")

package org.runestar.client.api

import org.runestar.client.common.ICON
import org.runestar.client.common.TITLE
import java.applet.Applet
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.WindowConstants
import kotlin.math.max

class GameFrame internal constructor(
        val applet: Applet
) : JFrame(TITLE) {

    val sidePanel = SidePanel()

    val topBar = TopBar()

    init {
        layout = BorderLayout()
        add(applet, BorderLayout.CENTER)
        add(sidePanel, BorderLayout.EAST)
        add(topBar, BorderLayout.NORTH)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        iconImage = ICON
        pack()
        minimumSize = layout.minimumLayoutSize(this)
        setLocationRelativeTo(owner)
        isVisible = true
    }

    internal fun refit() {
        // todo
        minimumSize = layout.minimumLayoutSize(this)
        val appletMinSize = applet.minimumSize
        applet.minimumSize = applet.size
        size = layout.minimumLayoutSize(this)
        revalidate()
        repaint()
        applet.minimumSize = appletMinSize
    }
}