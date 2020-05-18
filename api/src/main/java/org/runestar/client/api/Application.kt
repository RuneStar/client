package org.runestar.client.api

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.blockingSubscribeBy
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.kxtra.swing.input.isLeftButton
import org.runestar.client.api.util.safeIconImage
import org.runestar.client.api.util.safeMenu
import org.runestar.client.api.util.safeRequestWindowUserAttention
import org.runestar.client.api.util.safeSetWindowProgressState
import org.runestar.client.api.util.safeSetWindowProgressValue
import org.runestar.client.api.util.safeTrayIcon
import org.runestar.client.api.util.systemTray
import org.runestar.client.api.util.taskbar
import org.runestar.client.api.game.GameState
import org.runestar.client.api.game.live.Game
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XClient
import org.runestar.client.api.plugins.PluginLoader
import org.runestar.client.api.plugins.PropertiesFileReadWriter
import java.awt.Frame
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.Taskbar
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import javax.swing.ImageIcon
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

object Application : AutoCloseable {

    private const val DEFAULT_PROFILE = "default"

    private val VALID_PROFILE_REGEX = "[a-zA-Z0-9 _-]+".toRegex()

    val trayIcon: TrayIcon? = safeTrayIcon(ICON, TITLE).apply {
        this?.isImageAutoSize = true
    }

    @Volatile
    lateinit var frame: GameFrame
        private set

    private var pluginLoader: PluginLoader? = null

    private var started = false

    private var rsn: String? = null

    private var profileName: String = DEFAULT_PROFILE

    fun start() {
        check(!started)
        started = true

        SwingUtilities.invokeAndWait {
            @Suppress("DEPRECATION")
            frame = GameFrame(CLIENT as java.applet.Applet)
        }

        modifyClient()
        waitForTitle()

        setProfile(DEFAULT_PROFILE)

        buildTray()

        Observable.interval(2, TimeUnit.SECONDS).subscribe {
            val name = if (Game.state == GameState.TITLE) null else CLIENT.localPlayerName
            if (name != rsn) {
                rsn = name
                setTitle()
            }
        }
    }

    private fun waitForTitle() {
        val tb = taskbar
        XClient.doCycle.exit
                .map { ((CLIENT.titleLoadingStage.toDouble() / 150) * 100).toInt() }
                .takeUntil { it >= 100 }
                .blockingSubscribeBy(
                        onNext = { tb?.safeSetWindowProgressValue(frame, it) },
                        onComplete = {
                            tb?.safeSetWindowProgressState(frame, Taskbar.State.OFF)
                            tb?.safeRequestWindowUserAttention(frame)
                        }
                )
    }

    private fun modifyClient() {
        XClient.doCycle.enter.firstOrError().subscribeBy {

            CLIENT.gameDrawingMode = 2 // always redraw entire canvas

            // pre load worlds
            while (!CLIENT.loadWorlds()) {
                Thread.sleep(10)
            }

            Thread.currentThread().name = "client"
        }
    }

    private fun changeProfile() {
        val existingProfiles = ArrayList<String>()
        for (p in Files.newDirectoryStream(PROFILES_DIR_PATH)) {
            if (Files.isDirectory(p)) {
                val name = p.fileName.toString()
                if (name != profileName) {
                    existingProfiles.add(name)
                }
            }
        }
        val p = showProfileDialog(existingProfiles)
        if (p != null && p != profileName) {
            setProfile(p)
        }
    }

    // todo
    private fun showProfileDialog(profiles: List<String>): String? {
        val msg = "Current: $profileName\nOthers: [${profiles.joinToString()}]"
        return JOptionPane.showInputDialog(
                frame,
                msg,
                "Change profile",
                JOptionPane.PLAIN_MESSAGE,
                ImageIcon(ICON),
                null,
                profileName
        ).takeIf {
            it is String && !it.isBlank() && it.matches(VALID_PROFILE_REGEX)
        } as String?
    }

    private fun setProfile(profile: String) {
        profileName = profile
        pluginLoader?.close()
        val profileDir = PROFILES_DIR_PATH.resolve(profile)
        Files.createDirectories(profileDir)

        val pl = PluginLoader(profileDir, PropertiesFileReadWriter, Game.executor)
        pluginLoader = pl
        frame.sidePanel.clear()
        frame.sidePanel.panel.isVisible = false
        frame.refit()
        frame.sidePanel.addFirst(PluginsTab(pl.plugins))
    }

    private fun setTitle() {
        val title = rsn?.let { "$TITLE - $it" } ?: TITLE
        frame.title = title
        trayIcon?.toolTip = title
    }

    private fun buildTray() {
        val popMenu = PopupMenu(TITLE).apply {
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

        trayIcon?.apply {
            popupMenu = popMenu
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (e.isLeftButton) frame.state = Frame.NORMAL
                }
            })
            systemTray?.add(this)
        }

        taskbar?.apply {
            safeMenu = popMenu
            safeIconImage = ICON
        }
    }

    override fun close() {
        check(started)
        pluginLoader?.close()
        trayIcon?.let { systemTray?.remove(it) }
    }
}