@file:JvmName("ClientMain")

package com.runesuite.client

import com.runesuite.client.common.PLUGINS_DIR_PATH
import com.runesuite.client.common.PLUGINS_SETTINGS_DIR_PATH
import com.runesuite.client.game.api.live.Game
import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.access.XClient
import com.runesuite.client.pluginframework.PluginLoader
import com.runesuite.general.JavConfig
import com.runesuite.general.updateRevision
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import javax.swing.JFrame
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    System.setProperty("sun.awt.noerasebackground", true.toString())
    updateRevision()

    val javConfig = JavConfig()
    Client.accessor = Class.forName(javConfig.initialClass).getDeclaredConstructor().newInstance() as XClient
    @Suppress("DEPRECATION")
    val applet = Client.accessor as java.applet.Applet

    applet.preInit(javConfig)

    val jframe = JFrame(javConfig[JavConfig.Key.TITLE]).apply {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        add(applet)
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

    val pluginLoader = PluginLoader(PLUGINS_DIR_PATH, PLUGINS_SETTINGS_DIR_PATH)

    jframe.addWindowListener(object : WindowAdapter() {
        override fun windowClosed(e: WindowEvent) {
            pluginLoader.close()
            System.exit(0)
        }
    })
    jframe.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
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
    Game.stateChanges.takeUntil { it == Game.State.TITLE }
            .ignoreElements()
            .blockingAwait()
}