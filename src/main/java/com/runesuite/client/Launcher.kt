package com.runesuite.client

import com.runesuite.client.base.Client
import com.runesuite.client.base.access.XClient
import com.runesuite.client.game.live.Canvas
import com.runesuite.client.game.live.Game
import com.runesuite.client.game.live.Minimap
import com.runesuite.client.game.live.Players
import com.runesuite.general.JavConfig
import java.applet.Applet
import java.awt.Color
import java.awt.Dimension
import java.net.URLClassLoader
import java.nio.file.Paths
import javax.swing.JFrame
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    System.setProperty("sun.awt.noerasebackground", true.toString())

    val gamepack = Paths.get(System.getProperty("user.home"), "RuneSuite", "gamepacks", "runescape-gamepack.145.runesuite-inject.jar")

    val javConfig = JavConfig()

    val classLoader = URLClassLoader(arrayOf(gamepack.toUri().toURL()))
    Client.accessor = classLoader.loadClass(javConfig.initialClass).newInstance() as XClient
    val applet = Client.accessor as Applet

    applet.preInit(javConfig)

    JFrame(javConfig[JavConfig.Key.TITLE]).apply {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
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

    Canvas.Live.repaints.subscribe {
        it.color = Color.CYAN
        it.draw(Minimap.Live.circle)
        Players.local
    }
    Game.stateChanges.subscribe {
        println(it)
    }
}

private fun Applet.preInit(javConfig: JavConfig) {
    layout = null
    setStub(JavConfig.AppletStub(javConfig))
    minimumSize = Dimension(200, 350)
    maximumSize = javConfig.appletMaxSize
    preferredSize = javConfig.appletMinSize
    size = preferredSize
}