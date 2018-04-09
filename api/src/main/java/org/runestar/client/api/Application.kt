package org.runestar.client.api

import io.reactivex.Observable
import io.reactivex.plugins.RxJavaPlugins
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
import java.awt.Frame
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.TrayIcon
import java.awt.event.*
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import javax.swing.*

object Application {

    private val logger = getLogger()

    private const val DEFAULT_PROFILE = "default"

    private val VALID_PROFILE_REGEX = "[a-zA-Z0-9 _-]+".toRegex()

    val trayIcon: TrayIcon = TrayIcon(ICON, TITLE).apply {
        isImageAutoSize = true
    }

    @Volatile
    lateinit var frame: GameFrame
        private set

    private lateinit var pluginLoader: PluginLoader

    private var started = false

    fun start() {
        check(!started)
        started = true

        setup()

        val javConfig = JavConfig.load(System.getProperty("runestar.world", ""))
        Client.accessor = Class.forName(javConfig.initialClass).getDeclaredConstructor().newInstance() as XClient
        @Suppress("DEPRECATION")
        val applet = Client.accessor as java.applet.Applet

        SwingUtilities.invokeAndWait {
            buildApplet(applet, javConfig)
            frame = GameFrame(applet)
        }
        applet.init()
        applet.start()

        waitForTitle()
        Client.accessor.gameDrawingMode = 2

        setProfile()

        frame.addWindowListener(FrameCloseConfirmListener())

        trayIcon.apply {
            popupMenu = PopupMenu(TITLE).apply {
                add(MenuItem("Change profile").apply {
                    addActionListener { changeProfile() }
                })
                add(MenuItem("Toggle side panel").apply {
                    addActionListener {
                        frame.sidePanel.isVisible = !frame.sidePanel.isVisible
                        frame.refit()
                    }
                })
            }
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (e.isLeftButton) frame.state = Frame.NORMAL
                }
            })
        }

        systemTray?.add(trayIcon)
    }

    private fun setup() {
        AwtTaskbar.setIconImage(ICON)
        logger.info(systemDebugString)
        RxJavaPlugins.setErrorHandler {
            logger.warn("RxJavaPlugins error handler", it)
        }
        SwingUtilities.invokeLater(LafInstallation)
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

    private class FrameCloseConfirmListener : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            frame.defaultCloseOperation = if (Game.state == GameState.TITLE || confirmExit(frame)) {
                pluginLoader.close()
                systemTray?.remove(trayIcon)
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

    private fun changeProfile() {
        val existingCustomProfiles = ArrayList<String>()
        for (p in Files.newDirectoryStream(PROFILES_DIR_PATH)) {
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
        if (::pluginLoader.isInitialized) pluginLoader.close()
        val profileDir = PROFILES_DIR_PATH.resolve(profile)
        Files.createDirectories(profileDir)


        frame.sidePanel.clear()
        frame.topBar.clear()
        pluginLoader = PluginLoader(PLUGINS_DIR_PATH, profileDir, YamlFileReadWriter)
        frame.sidePanel.add(PluginsTab(pluginLoader))
        frame.sidePanel.add(HideTopBarButton())
    }
}