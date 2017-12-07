@file:JvmName("ClientMain")

package com.runesuite.client

import com.alee.laf.WebLookAndFeel
import com.runesuite.client.common.PLUGINS_DIR_PATH
import com.runesuite.client.common.PLUGINS_JARS_DIR_PATH
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
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

private val logger = getLogger()

private val classLoader = MethodHandles.lookup().lookupClass().classLoader

internal val ICON = ImageIO.read(classLoader.getResource("icon.png")).toCompatibleImage()

private const val TITLE = "RuneSuite"

fun main(args: Array<String>) {
    System.setProperty("sun.awt.noerasebackground", true.toString())

    SwingUtilities.invokeLater {
        if (!WebLookAndFeel.install()) {
            logger.warn("Failed to install Web Look and Feel")
        }
    }

    val javConfig = JavConfig.load()
    Client.accessor = Class.forName(javConfig.initialClass).getDeclaredConstructor().newInstance() as XClient
    @Suppress("DEPRECATION")
    val applet = Client.accessor as java.applet.Applet
    applet.preInit(javConfig)

    var pluginsWindow: PluginsWindow? = null
    lateinit var frame: JFrame

    SwingUtilities.invokeAndWait {
        frame = JFrame(TITLE).apply {
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

    applet.apply {
        init()
        start()
    }

    waitForTitle()

    val pluginLoader = PluginLoader(PLUGINS_JARS_DIR_PATH, PLUGINS_DIR_PATH, YamlFileReadWriter)

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
                state = Frame.NORMAL
                requestFocus()
            }
        }
    }

    SwingUtilities.invokeLater {
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                pluginLoader.close()
            }
        })
    }

    val trayIcon = TrayIcon(
            ICON,
            TITLE,
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

@Suppress("DEPRECATION")
private fun java.applet.Applet.preInit(javConfig: JavConfig) {
    layout = null
    setStub(JavConfig.AppletStub(javConfig))
    minimumSize = Dimension(200, 350)
    maximumSize = javConfig.appletMaxSize
    preferredSize = javConfig.appletMinSize
    size = preferredSize
}

private fun waitForTitle() {
    // wait for most fields to be initialized
    Game.stateChanges.takeUntil { it == GameState.TITLE }
            .ignoreElements()
            .blockingAwait()
}