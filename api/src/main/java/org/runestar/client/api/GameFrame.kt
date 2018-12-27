@file:Suppress("DEPRECATION")

package org.runestar.client.api

import org.runestar.client.common.JAV_CONFIG
import org.runestar.client.common.JavConfig
import org.runestar.client.game.api.GameState
import org.runestar.client.game.raw.CLIENT
import java.applet.Applet
import java.awt.Color
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

class GameFrame internal constructor(
        val applet: Applet
) : JFrame(TITLE) {

    val sidePanel = SidePanel()

    val topBar = TopBar()

    init {
        buildApplet(applet)
        layout = BoxLayout(contentPane, BoxLayout.X_AXIS)
        add(
                Box.createVerticalBox().apply {
                    add(topBar)
                    add(applet)
                }
        )
        add(sidePanel)
        addWindowListener(WindowListener())
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        iconImage = ICON
        pack()
        minimumSize = layout.minimumLayoutSize(this)
        setLocationRelativeTo(owner)
        isVisible = true

        applet.init()
        applet.start()
    }

    private fun buildApplet(
            applet: Applet
    ) {
        applet.apply {
            layout = null
            setStub(JavConfig.AppletStub(JAV_CONFIG))
            maximumSize = JAV_CONFIG.appletMaxSize
            preferredSize = JAV_CONFIG.appletMinSize
            minimumSize = preferredSize
            background = Color.BLACK
            foreground = Color.BLACK
            size = preferredSize
        }
    }

    fun refit() {
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

    private inner class WindowListener : WindowAdapter() {

        override fun windowClosing(e: WindowEvent) {
            if (CLIENT.gameState <= GameState.TITLE.id || confirmExit()) {
                isVisible = false
                Application.close()
                dispose()
                defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            }
        }

        private fun confirmExit(): Boolean {
            return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
                    this@GameFrame,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION
            )
        }
    }
}