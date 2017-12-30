package org.runestar.client.plugins.std

import org.kxtra.swing.bufferedimage.BufferedImage
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XRasterProvider
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.event.KeyEvent
import java.nio.file.Files
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

class ScreenShots : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    val fileExtension = "png"

    val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.kk-mm-ss,SSS")
            .withZone(ZoneId.systemDefault())

    override fun start() {
        super.start()

        val screenShotDirectory = directory.resolve("screenshots")

        add(Keyboard.events.subscribe { ke ->
            if (ke.extendedKeyCode == KeyEvent.VK_PRINTSCREEN) {
                XRasterProvider.drawFull0.exit.firstOrError().subscribe { e ->
                    val rsn = Client.accessor.localPlayerName
                    val timeString = timeFormatter.format(Instant.now())
                    val fileName = "$rsn.$timeString.$fileExtension"
                    val image = BufferedImage(e.instance.image)
                    val path = screenShotDirectory.resolve(fileName)
                    Files.createDirectories(path)
                    ImageIO.write(image, fileExtension, path.toFile())
                }
            }
        })
    }
}