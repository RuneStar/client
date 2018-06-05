package org.runestar.client.api

import org.runestar.client.game.api.GameState
import org.runestar.client.game.raw.Client
import java.awt.Container
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

class GameFrame internal constructor(
        val applet: Container
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
        addWindowListener(WindowListener())
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        iconImage = ICON
        pack()
        minimumSize = layout.minimumLayoutSize(this)
        setLocationRelativeTo(owner)
        isVisible = true
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
            if (Client.accessor.gameState <= GameState.TITLE.id || confirmExit()) {
                Application.close()
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