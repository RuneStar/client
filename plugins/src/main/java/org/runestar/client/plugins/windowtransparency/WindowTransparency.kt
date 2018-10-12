package org.runestar.client.plugins.windowtransparency

import org.runestar.client.api.Application
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Component
import javax.swing.SwingUtilities

/**
 * Tested on:
 *
 * Windows 10 - Java 8, 9, 10, 11
 *
 */
class WindowTransparency : AbstractPlugin<WindowTransparency.Settings>() {

    /**
     * Use reflection to bypass [java.awt.Window.setOpacity] checks for [java.awt.Frame.isUndecorated]
     */

    private companion object {

        // public peer method removed in Java 9
        val componentPeerField by lazy { Component::class.java.getDeclaredField("peer").apply { isAccessible = true } }

        // public in Java 8 but not exported in 9+
        val windowPeerClass by lazy { Class.forName("java.awt.peer.WindowPeer") }

        val setOpacityMethod by lazy { windowPeerClass.getDeclaredMethod("setOpacity", java.lang.Float.TYPE) }
    }

    override val defaultSettings = Settings()

    override val name = "Window Transparency"

    override fun start() {
        setOpacity(settings.opacity)
    }

    override fun stop() {
        setOpacity(1f)
    }

    private fun setOpacity(opacity: Float) {
        SwingUtilities.invokeLater {
            val frame = Application.frame
            synchronized(frame.treeLock) {
                val peer = componentPeerField.get(frame)
                setOpacityMethod.invoke(peer, opacity)
            }
        }
    }

    class Settings(val opacity: Float = 0.3f) : PluginSettings()
}