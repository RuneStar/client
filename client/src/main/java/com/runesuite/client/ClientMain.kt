@file:JvmName("ClientMain")

package com.runesuite.client

import com.alee.laf.WebLookAndFeel
import com.alee.laf.button.WebButton
import com.runesuite.client.common.PLUGINS_DIR_PATH
import com.runesuite.client.common.PLUGINS_JARS_DIR_PATH
import com.runesuite.client.game.api.GameState
import com.runesuite.client.game.api.live.Game
import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.access.XClient
import com.runesuite.client.plugins.PluginLoader
import com.runesuite.general.JavConfig
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.Box
import javax.swing.JFrame
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    System.setProperty("sun.awt.noerasebackground", true.toString())
    WebLookAndFeel.install()

    val javConfig = JavConfig()
    Client.accessor = Class.forName(javConfig.initialClass).getDeclaredConstructor().newInstance() as XClient
    @Suppress("DEPRECATION")
    val applet = Client.accessor as java.applet.Applet
    applet.preInit(javConfig)

    val pluginsButton = WebButton("plugins")
    var pluginsWindow: PluginsWindow? = null

    val jframe = JFrame(javConfig[JavConfig.Key.TITLE]).apply {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        add(Box.createHorizontalBox().apply {
            add(Box.createGlue())
            add(pluginsButton)
        }, BorderLayout.NORTH)
        add(applet, BorderLayout.CENTER)
        pack()
        setLocationRelativeTo(null)
        isVisible = true
        preferredSize = size
        minimumSize = applet.minimumSize
    }

    applet.apply {
        init()
        start()
    }

    waitForTitle()

    val pluginLoader = PluginLoader(PLUGINS_JARS_DIR_PATH, PLUGINS_DIR_PATH)

    jframe.apply {
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                pluginLoader.close()
            }
        })
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }

    pluginsButton.addActionListener {
        val pw = pluginsWindow
        if (pw == null) {
            pluginsWindow = PluginsWindow(pluginLoader).apply {
                addWindowListener(object : WindowAdapter() {
                    override fun windowClosing(e: WindowEvent?) {
                        pluginsWindow = null
                    }
                })
            }
        } else {
            pw.requestFocus()
        }
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