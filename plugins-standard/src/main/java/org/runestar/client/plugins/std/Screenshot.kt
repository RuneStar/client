package org.runestar.client.plugins.std

import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XRasterProvider
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.event.KeyEvent
import java.awt.image.RenderedImage
import java.nio.file.Files
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

class Screenshot : DisposablePlugin<Screenshot.Settings>() {

    companion object {
        const val IMAGE_FILE_EXTENSION = "png"
        const val SCREENSHOTS_DIRECTORY_NAME = "screenshots"
    }

    override val defaultSettings = Settings()

    override fun start() {
        super.start()

        val zoneId = if (settings.localizeTimeZone) ZoneId.systemDefault() else ZoneId.from(ZoneOffset.UTC)
        val timeFormatter = DateTimeFormatter.ofPattern(settings.dateTimeFormatterPattern)
                .withZone(zoneId)

        val screenShotDirectory = directory.resolve(SCREENSHOTS_DIRECTORY_NAME)

        add(Keyboard.events.subscribe { ke ->
            if (ke.extendedKeyCode == KeyEvent.VK_PRINTSCREEN && ke.id == KeyEvent.KEY_RELEASED) {
                XRasterProvider.drawFull0.exit.firstOrError().subscribe { e ->
                    val rsn = Client.accessor.localPlayerName
                    val timeString = timeFormatter.format(Instant.now())
                    val fileName = "$rsn.$timeString.$IMAGE_FILE_EXTENSION"
                    val image = e.instance.image as RenderedImage
                    val path = screenShotDirectory.resolve(fileName)
                    Files.createDirectories(path)
                    ImageIO.write(image, IMAGE_FILE_EXTENSION, path.toFile())
                }
            }
        })
    }

    class Settings : PluginSettings() {

        val dateTimeFormatterPattern = "yyyy-MM-dd'T'kk-mm-ss,SSS"
        val localizeTimeZone = true
    }
}