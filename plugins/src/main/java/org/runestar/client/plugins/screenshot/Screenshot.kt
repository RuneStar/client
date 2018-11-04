package org.runestar.client.plugins.screenshot

import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.kxtra.swing.image.BufferedImage
import org.runestar.client.api.Application
import org.runestar.client.api.BarButton
import org.runestar.client.api.forms.KeyStrokeForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XRasterProvider
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.TrayIcon
import java.awt.event.ActionEvent
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO
import javax.swing.Icon
import javax.swing.ImageIcon

class Screenshot : DisposablePlugin<Screenshot.Settings>() {

    private companion object {
        const val IMAGE_FILE_EXTENSION = "png"
        const val SCREENSHOTS_DIRECTORY_NAME = "screenshots"
    }

    override val defaultSettings = Settings()

    private lateinit var timeFormatter: DateTimeFormatter
    private lateinit var screenshotDirectory: Path

    private val button = Button()

    override fun start() {
        timeFormatter = createTimeFormatter()
        screenshotDirectory = ctx.directory.resolve(SCREENSHOTS_DIRECTORY_NAME)

        Keyboard.strokes
                .filter(settings.keyStroke.get()::equals)
                .delay { XRasterProvider.drawFull0.exit }
                .map { copyCanvas() }
                .observeOn(Schedulers.io())
                .subscribe { saveImage(it) }
                .add()

        Application.frame.topBar.add(button)
    }

    override fun stop() {
        super.stop()
        Application.frame.topBar.remove(button)
    }

    private fun createTimeFormatter(): DateTimeFormatter {
        val zoneId = if (settings.localizeTimeZone) ZoneId.systemDefault() else ZoneOffset.UTC
        return DateTimeFormatter.ofPattern(settings.dateTimeFormatterPattern)
                .withZone(zoneId)
    }

    private fun copyCanvas(): BufferedImage {
        val rp = CLIENT.rasterProvider as XRasterProvider
        val img = rp.image as BufferedImage
        return BufferedImage(img)
    }

    private fun saveImage(img: RenderedImage) {
        val rsn = CLIENT.localPlayerName
        val timeString = timeFormatter.format(Instant.now())
        val fileName = "$rsn.$timeString.$IMAGE_FILE_EXTENSION"
        val path = screenshotDirectory.resolve(fileName)
        try {
            Files.createDirectories(path)
            ImageIO.write(img, IMAGE_FILE_EXTENSION, path.toFile())
            if (settings.trayNotify) {
                Application.trayIcon.displayMessage(
                        "Screenshot Taken",
                        fileName,
                        TrayIcon.MessageType.NONE
                )
            }
        } catch (e: IOException) {
            logger.error("Failed to take screenshot", e)
        }
    }

    data class Settings(
            val keyStroke: KeyStrokeForm = KeyStrokeForm("released PRINTSCREEN"),
            val dateTimeFormatterPattern: String = "yyyy-MM-dd_kk-mm-ss,SSS",
            val localizeTimeZone: Boolean = true,
            val trayNotify: Boolean = true
    ) : PluginSettings()

    inner class Button : BarButton() {

        override val name: String = "Screenshot"

        override val icon: Icon = ImageIcon(ImageIO.read(javaClass.getResource("camera.png")))

        override fun actionPerformed(e: ActionEvent) {
            XRasterProvider.drawFull0.exit.firstOrError()
                    .map { copyCanvas() }
                    .observeOn(Schedulers.io())
                    .subscribeBy { saveImage(it) }
        }
    }
}