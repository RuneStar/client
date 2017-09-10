@file:JvmName("Loader")

package com.runesuite.client.core

import com.runesuite.client.core.api.live.Game
import com.runesuite.client.core.raw.Client
import com.runesuite.client.core.raw.access.XClient
import com.runesuite.client.plugins.framework.PluginLoader
import com.runesuite.client.plugins.framework.URLClassLoader
import com.runesuite.client.shared.GAMEPACK
import com.runesuite.client.shared.PLUGINS_DIR
import com.runesuite.client.shared.PLUGINS_SETTINGS_DIR
import com.runesuite.general.JavConfig
import com.runesuite.general.updateRevision
import java.applet.Applet
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    System.setProperty("sun.awt.noerasebackground", true.toString())
    updateRevision()

    val javConfig = JavConfig()

    val classLoader = URLClassLoader(GAMEPACK)
    Client.accessor = classLoader.loadClass(javConfig.initialClass).newInstance() as XClient
    val applet = Client.accessor as Applet

    applet.preInit(javConfig)

    val jframe = JFrame(javConfig[JavConfig.Key.TITLE]).apply {
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
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

    val pluginLoader = PluginLoader(PLUGINS_DIR, PLUGINS_SETTINGS_DIR)

    jframe.addWindowListener(object : WindowAdapter() {
        override fun windowClosed(e: WindowEvent) {
            pluginLoader.close()
            System.exit(0)
        }
    })
}

private fun Applet.preInit(javConfig: JavConfig) {
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