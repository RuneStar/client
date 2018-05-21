package org.runestar.client.api

import io.reactivex.Observable
import io.reactivex.rxkotlin.blockingSubscribeBy
import org.kxtra.slf4j.loggerfactory.getLogger
import org.kxtra.swing.mouseevent.isLeftButton
import org.runestar.client.api.util.AwtTaskbar
import org.runestar.client.api.util.systemTray
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.WidgetGroupId
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.access.XWidgetGroupParent
import org.runestar.client.plugins.spi.PluginLoader
import org.runestar.general.JavConfig
import java.awt.Frame
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import javax.swing.ImageIcon
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

object Application : AutoCloseable {

    private val logger = getLogger()

    private const val DEFAULT_PROFILE = "default"

    private val VALID_PROFILE_REGEX = "[a-zA-Z0-9 _-]+".toRegex()

    val trayIcon: TrayIcon = TrayIcon(ICON, TITLE).apply {
        isImageAutoSize = true
    }

    @Volatile
    lateinit var frame: GameFrame
        private set

    private var pluginLoader: PluginLoader? = null

    private var started = false

    private var rsn: String? = null

    private var profileName: String = DEFAULT_PROFILE

    fun start(
            javConfig: JavConfig,
            gamepack: ClassLoader
    ) {
        check(!started)
        started = true

        Client.accessor = gamepack.loadClass(javConfig.initialClass).getDeclaredConstructor().newInstance() as XClient
        @Suppress("DEPRECATION")
        val applet = Client.accessor as java.applet.Applet

        SwingUtilities.invokeAndWait {
            buildApplet(applet, javConfig)
            frame = GameFrame(applet)
        }
        applet.init()
        applet.start()

        waitForTitle()
        modifyClient()

        setProfile(DEFAULT_PROFILE)

        buildTray()

        Observable.interval(2, TimeUnit.SECONDS).subscribe {
            val name = if (Game.state == GameState.TITLE) null else Client.accessor.localPlayerName
            if (name != rsn) {
                rsn = name
                setTitle()
            }
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

    private fun modifyClient() {
        Client.accessor.gameDrawingMode = 2

        XClient.closeWidgetGroup.exit.subscribe {
            val wgp = it.arguments[0] as XWidgetGroupParent
            val group = wgp.group
            if (group == WidgetGroupId.WorldMap.id) {
                val wm = Client.accessor.worldMap
                wm.initializeWorldMapManager(wm.worldMapData)
                System.gc()
            }
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

        val pl = PluginLoader(javaClass.classLoader, profileDir, YamlFileReadWriter)
        pluginLoader = pl
        frame.sidePanel.clear()
        frame.topBar.clear()
        frame.sidePanel.panel.isVisible = false
        frame.refit()
        frame.sidePanel.addFirst(PluginsTab(pl))
        frame.sidePanel.add(HideTopBarButton())
    }

    private fun setTitle() {
        val title = rsn?.let { "$TITLE - $it" } ?: TITLE
        frame.title = title
        trayIcon.toolTip = title
    }

    private fun buildTray() {
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

    override fun close() {
        check(started)
        frame.dispose()
        pluginLoader?.close()
        systemTray?.remove(trayIcon)
    }
}