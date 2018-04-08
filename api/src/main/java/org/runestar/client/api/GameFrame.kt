@file:Suppress("DEPRECATION")

package org.runestar.client.api

import org.runestar.client.common.ICON
import org.runestar.client.common.TITLE
import java.applet.Applet
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.WindowConstants

class GameFrame internal constructor(
        applet: Applet
) : JFrame(TITLE) {

    val sidePanel = SidePanel()

    val topBar = TopBar()

    init {
        layout = BorderLayout()
        add(applet, BorderLayout.CENTER)
        add(sidePanel, BorderLayout.EAST)
//        add(topBar, BorderLayout.NORTH)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        iconImage = ICON
        pack()
        minimumSize = layout.minimumLayoutSize(this)
        setLocationRelativeTo(owner)
        isVisible = true
    }

    internal fun toggleSidePanelVisibility() {
        val startSize = size
        if (sidePanel.isShowing) {
            remove(sidePanel)
            minimumSize = layout.minimumLayoutSize(this)
            size = Dimension(startSize.width - SidePanel.WIDTH, startSize.height)
        } else {
            add(sidePanel, BorderLayout.EAST)
            minimumSize = layout.minimumLayoutSize(this)
            size = Dimension(startSize.width + SidePanel.WIDTH, startSize.height)
        }
        revalidate()
        repaint()
    }

    internal fun toggleTopBarVisibility() {
        val startSize = size
        if (topBar.isShowing) {
            remove(topBar)
            minimumSize = layout.minimumLayoutSize(this)
            size = Dimension(startSize.width, startSize.height - TopBar.HEIGHT)
        } else {
            add(topBar, BorderLayout.NORTH)
            minimumSize = layout.minimumLayoutSize(this)
            size = Dimension(startSize.width, startSize.height + TopBar.HEIGHT)
        }
        revalidate()
        repaint()
    }
}