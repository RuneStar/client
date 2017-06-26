package com.runesuite.client

import com.runesuite.client.base.Client
import com.runesuite.client.base.access.XClient
import com.runesuite.client.dev.plugins.PluginLoader
import com.runesuite.client.dev.plugins.newJarClassLoader
import com.runesuite.general.JavConfig
import com.runesuite.general.RuneScape
import java.applet.Applet
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JFrame
import javax.swing.WindowConstants

val RUNESUITE_DIR = Paths.get(System.getProperty("user.home"), "RuneSuite").apply {
    Files.createDirectories(this)
}

val PLUGINS_DIR = RUNESUITE_DIR.resolve("plugins").apply {
    Files.createDirectories(this)
}

val PLUGINS_SETTINGS_DIR = RUNESUITE_DIR.resolve("plugins-settings").apply {
    Files.createDirectories(this)
}

val GAMEPACKS_DIR = RUNESUITE_DIR.resolve("gamepacks").apply {
    Files.createDirectories(this)
}

fun main(args: Array<String>) {
    System.setProperty("sun.awt.noerasebackground", true.toString())
    RuneScape.updateRevision()

    val gamepack = GAMEPACKS_DIR.resolve("runescape-gamepack.${RuneScape.revision}.runesuite-inject.jar")

    val javConfig = JavConfig()

    val classLoader = newJarClassLoader(gamepack)
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

    Client.accessor.gameDrawingMode = 2

    applet.apply {
        init()
        start()
    }

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