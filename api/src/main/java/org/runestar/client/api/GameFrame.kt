@file:Suppress("DEPRECATION")

package org.runestar.client.api

import io.reactivex.disposables.Disposable
import org.runestar.client.api.util.Disposable
import org.runestar.client.common.JAV_CONFIG
import org.runestar.client.api.game.GameState
import org.runestar.client.raw.CLIENT
import java.applet.Applet
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class GameFrame internal constructor(
        val applet: Applet
) : JFrame(TITLE) {

    internal val sidePanel = SidePanel()

    init {
        buildApplet(applet)
        layout = BoxLayout(contentPane, BoxLayout.X_AXIS)
        add(applet)
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
            setStub(JAV_CONFIG)
            maximumSize = JAV_CONFIG.appletMaxSize
            preferredSize = JAV_CONFIG.appletMinSize
            minimumSize = preferredSize
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
        val side = sidePanel.size

        minimumSize = layout.minimumLayoutSize(this)
        size = Dimension(
                app.width + insets.width + side.width,
                app.height + insets.height
        )

        repaint()
    }

    fun register(tabButton: TabButton): Disposable {
        check(SwingUtilities.isEventDispatchThread())
        sidePanel.add(tabButton)
        return Disposable { SwingUtilities.invokeLater { sidePanel.remove(tabButton) } }
    }

    fun register(actionButton: ActionButton): Disposable {
        check(SwingUtilities.isEventDispatchThread())
        sidePanel.add(actionButton)
        return Disposable { SwingUtilities.invokeLater { sidePanel.remove(actionButton) } }
    }

    private inner class WindowListener : WindowAdapter() {

        override fun windowClosing(e: WindowEvent) {
            if (CLIENT.gameState <= GameState.TITLE || confirmExit()) {
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