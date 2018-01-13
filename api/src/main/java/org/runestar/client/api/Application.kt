package org.runestar.client.api

import com.alee.laf.WebLookAndFeel
import io.reactivex.Observable
import io.reactivex.rxkotlin.blockingSubscribeBy
import org.kxtra.slf4j.loggerfactory.getLogger
import org.kxtra.swing.mouseevent.isLeftButton
import org.runestar.client.common.*
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.plugins.PluginLoader
import org.runestar.general.JavConfig
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import javax.swing.*

object Application {

    private const val DEFAULT_PROFILE = "default"

    private val VALID_PROFILE_REGEX = "[a-zA-Z0-9 _-]+".toRegex()

    private val logger = getLogger()

    val trayIcon = TrayIcon(ICON, TITLE).apply {
        isImageAutoSize = true
    }

    lateinit var frame: JFrame
        private set

    private lateinit var pluginLoader: PluginLoader

    private var pluginsWindow: PluginsWindow? = null

    private var started = false

    fun start() {
        check(!started)
        started = true

        systemStartUp()

        val javConfig = JavConfig.load()
        Client.accessor = Class.forName(javConfig.initialClass).getDeclaredConstructor().newInstance() as XClient
        @Suppress("DEPRECATION")
        val applet = Client.accessor as java.applet.Applet

        appletPreInit(applet, javConfig)
        SwingUtilities.invokeAndWait { frame = newGameWindow(applet) }
        applet.init()
        applet.start()

        waitForTitle()

        setProfile()

        frame.addWindowListener(FrameCloseConfirmListener())

        trayIcon.apply {
            popupMenu = PopupMenu(TITLE).apply {
                add(MenuItem("Change profile").apply {
                    addActionListener { changeProfile() }
                })
                add(MenuItem("Plugins").apply {
                    addActionListener { openPluginsAction() }
                })
            }
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (e.isLeftButton) frame.state = Frame.NORMAL
                }
            })
        }

        try {
            SystemTray.getSystemTray().add(trayIcon)
        } catch (e: Exception) {
            logger.warn("Unable to use system tray")
        }
    }

    private fun systemStartUp() {
        System.setProperty("sun.awt.noerasebackground", true.toString())
        SwingUtilities.invokeLater {
            if (!WebLookAndFeel.install()) {
                logger.warn("Failed to install Web Look and Feel")
            }
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
        Observable.interval(20, TimeUnit.MILLISECONDS)
                .map { ((Client.accessor.titleLoadingStage.toDouble() / 150) * 100).toInt() }
                .takeUntil { it >= 100 }
                .blockingSubscribeBy(
                        onNext = { AwtTaskbar.setWindowProgressValue(frame, it) },
                        onComplete = {
                            AwtTaskbar.setWindowProgressState(frame, AwtTaskbar.State.OFF)
                            AwtTaskbar.requestWindowUserAttention(frame)
                        }
                )
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

    private class FrameCloseConfirmListener : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            frame.defaultCloseOperation = if (Game.state == GameState.TITLE || confirmExit(frame)) {
                pluginLoader.close()
                WindowConstants.EXIT_ON_CLOSE
            } else {
                WindowConstants.DO_NOTHING_ON_CLOSE
            }
        }
    }

    private fun confirmExit(frame: Frame): Boolean {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to exit?",
                "Exit",
                JOptionPane.YES_NO_OPTION
        )
    }

    private fun openPluginsAction() {
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

    private fun changeProfile() {
        val existingCustomProfiles = ArrayList<String>()
        for (p in Files.newDirectoryStream(PLUGINS_DIR_PATH)) {
            if (Files.isDirectory(p)) {
                val name = p.fileName.toString()
                if (name != DEFAULT_PROFILE) {
                    existingCustomProfiles.add(name)
                }
            }
        }
        val p = showProfileDialog(existingCustomProfiles)
        if (p != null) {
            setProfile(p)
        }
    }

    // todo
    private fun showProfileDialog(profiles: List<String>): String? {
        val msg = "Existing profiles: [${profiles.joinToString()}]"
        return JOptionPane.showInputDialog(
                frame,
                msg,
                "Change profile",
                JOptionPane.PLAIN_MESSAGE,
                ImageIcon(ICON),
                null,
                DEFAULT_PROFILE
        ).takeIf {
            it is String && !it.isBlank() && it.matches(VALID_PROFILE_REGEX)
        } as String?
    }

    private fun setProfile(profile: String = DEFAULT_PROFILE) {
        val title = if (profile == DEFAULT_PROFILE) TITLE else "$TITLE - $profile"
        frame.title = title
        trayIcon.toolTip = title
        pluginsWindow?.dispose()
        if (::pluginLoader.isInitialized) pluginLoader.close()
        val profileDir = PLUGINS_DIR_PATH.resolve(profile)
        Files.createDirectories(profileDir)
        pluginLoader = PluginLoader(PLUGINS_JARS_DIR_PATH, profileDir, YamlFileReadWriter)
    }
}