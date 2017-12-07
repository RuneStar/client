@file:JvmName("ClientMain")

package com.runesuite.client

import com.alee.laf.WebLookAndFeel
import com.runesuite.client.common.PLUGINS_DIR_PATH
import com.runesuite.client.common.PLUGINS_JARS_DIR_PATH
import com.runesuite.client.common.TITLE
import com.runesuite.client.game.api.GameState
import com.runesuite.client.game.api.live.Game
import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.access.XClient
import com.runesuite.client.plugins.PluginLoader
import com.runesuite.general.JavConfig
import org.kxtra.slf4j.loggerfactory.getLogger
import org.kxtra.swing.bufferedimage.toCompatibleImage
import org.kxtra.swing.mouseevent.isLeftButton
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.lang.invoke.MethodHandles
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

private val logger = getLogger()

private val classLoader = MethodHandles.lookup().lookupClass().classLoader

internal val ICON = ImageIO.read(classLoader.getResource("icon.png")).toCompatibleImage()

fun main(args: Array<String>) {
    systemStartUp()

    val javConfig = JavConfig.load()
    Client.accessor = Class.forName(javConfig.initialClass).getDeclaredConstructor().newInstance() as XClient
    @Suppress("DEPRECATION")
    val applet = Client.accessor as java.applet.Applet
    appletPreInit(applet, javConfig)

    lateinit var frame: JFrame
    SwingUtilities.invokeAndWait {
        frame = newGameWindow(applet)
    }

    applet.apply {
        init()
        start()
    }

    waitForTitle()

    val pluginLoader = PluginLoader(PLUGINS_JARS_DIR_PATH, PLUGINS_DIR_PATH, YamlFileReadWriter)

    var pluginsWindow: PluginsWindow? = null

    fun openPluginsAction() {
        pluginsWindow.apply {
            if (this == null) {
                pluginsWindow = PluginsWindow(pluginLoader).apply {
                    addWindowListener(object : WindowAdapter() {
                        override fun windowClosing(e: WindowEvent) {
                            pluginsWindow = null
                        }
                    })
                }
            } else {
                state = JFrame.NORMAL
                requestFocus()
            }
        }
    }

    SwingUtilities.invokeLater {
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                frame.defaultCloseOperation = if (Game.state == GameState.TITLE || confirmExit(frame)) {
                    pluginLoader.close()
                    WindowConstants.EXIT_ON_CLOSE
                } else {
                    WindowConstants.DO_NOTHING_ON_CLOSE
                }
            }
        })
    }

    val trayIcon = TrayIcon(ICON, TITLE,
            PopupMenu(TITLE).apply {
                add(MenuItem("Plugins").apply {
                    addActionListener { openPluginsAction() }
                })
            }
    ).apply {
        isImageAutoSize = true
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.isLeftButton) frame.state = Frame.NORMAL
            }
        })
    }
    try {
        SystemTray.getSystemTray().add(trayIcon)
    } catch (e: Exception) {
        logger.warn("failed to add to system tray", e)
    }
}

private fun appletPreInit(
        @Suppress("DEPRECATION") applet: java.applet.Applet,
        javConfig: JavConfig
) {
    applet.apply {
        layout = null
        setStub(JavConfig.AppletStub(javConfig))
        minimumSize = Dimension(200, 350)
        maximumSize = javConfig.appletMaxSize
        preferredSize = javConfig.appletMinSize
        size = preferredSize
    }
}

private fun waitForTitle() {
    // wait for most fields to be initialized
    Game.stateChanges.takeUntil { it == GameState.TITLE }
            .ignoreElements()
            .blockingAwait()
}

private fun newGameWindow(applet: Component): JFrame {
    return JFrame(TITLE).apply {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        add(applet)
        iconImage = ICON
        pack()
        setLocationRelativeTo(null)
        isVisible = true
        preferredSize = size
        minimumSize = applet.minimumSize
    }
}

private fun confirmExit(frame: Frame): Boolean {
    val option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION)
    return option == JOptionPane.YES_OPTION
}

private fun systemStartUp() {
    System.setProperty("sun.awt.noerasebackground", true.toString())
    SwingUtilities.invokeLater {
        if (!WebLookAndFeel.install()) {
            logger.warn("Failed to install Web Look and Feel")
        }
    }
}