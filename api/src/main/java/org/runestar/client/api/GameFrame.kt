@file:Suppress("DEPRECATION")

package org.runestar.client.api

import org.runestar.client.common.ICON
import org.runestar.client.common.TITLE
import java.applet.Applet
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.WindowConstants

class GameFrame internal constructor(
        val applet: Applet
) : JFrame(TITLE) {

    val sidePanel = SidePanel()

    val topBar = TopBar()

    init {
        layout = BoxLayout(contentPane, BoxLayout.X_AXIS)
        add(
                Box.createVerticalBox().apply {
                    add(topBar)
                    add(applet)
                }
        )
        add(sidePanel)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        iconImage = ICON
        pack()
        minimumSize = layout.minimumLayoutSize(this)
        setLocationRelativeTo(owner)
        isVisible = true
    }

    internal fun refit() {
        // todo

        val app = applet.size

        revalidate()

        val insets = insets.let {
            Dimension(it.left + it.right, it.top + it.bottom)
        }
        val top = topBar.size
        val side = sidePanel.size

        minimumSize = layout.minimumLayoutSize(this)
        size = Dimension(
                app.width + insets.width + side.width,
                app.height + insets.height + top.height
        )

        repaint()
    }
}