package org.runestar.client.plugins.screenshot

import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import org.kxtra.swing.image.BufferedImage
import org.runestar.client.api.ActionButton
import org.runestar.client.api.Application
import org.runestar.client.api.forms.DateTimeFormatterForm
import org.runestar.client.api.forms.KeyStrokeForm
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Keyboard
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XRasterProvider
import org.runestar.client.api.plugins.PluginSettings
import java.awt.TrayIcon
import java.awt.event.ActionEvent
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import javax.imageio.ImageIO
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.SwingUtilities

class Screenshot : DisposablePlugin<Screenshot.Settings>() {

    private companion object {
        const val IMAGE_FILE_EXTENSION = "png"
        const val SCREENSHOTS_DIRECTORY_NAME = "screenshots"
    }

    override val defaultSettings = Settings()

    private lateinit var screenshotDirectory: Path

    private val button = Button()

    override fun onStart() {
        screenshotDirectory = ctx.directory.resolve(SCREENSHOTS_DIRECTORY_NAME)

        Keyboard.strokes
                .filter(settings.keyStroke.value::equals)
                .delay { XRasterProvider.drawFull0.exit }
                .map { copyCanvas() }
                .observeOn(Schedulers.io())
                .subscribe { saveImage(it) }
                .add()

        SwingUtilities.invokeLater { add(Application.frame.register(Button())) }
    }

    private fun copyCanvas(): BufferedImage {
        val rp = CLIENT.rasterProvider as XRasterProvider
        val img = rp.image as BufferedImage
        return BufferedImage(img)
    }

    private fun saveImage(img: RenderedImage) {
        val rsn = CLIENT.localPlayerName
        val timeString = settings.dateTimeFormatter.value.format(Instant.now())
        val fileName = "$rsn.$timeString.$IMAGE_FILE_EXTENSION"
        val path = screenshotDirectory.resolve(fileName)
        try {
            Files.createDirectories(path)
            ImageIO.write(img, IMAGE_FILE_EXTENSION, path.toFile())
            if (settings.trayNotify) {
                Application.trayIcon?.displayMessage(
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
            val dateTimeFormatter: DateTimeFormatterForm = DateTimeFormatterForm("yyyy-MM-dd_HH-mm-ss,SSS", null),
            val trayNotify: Boolean = true
    ) : PluginSettings()

    inner class Button : ActionButton() {

        override val name: String = this@Screenshot.name

        override val icon: Icon = ImageIcon(ImageIO.read(javaClass.getResource("camera.png")))

        override fun actionPerformed(e: ActionEvent) {
            XRasterProvider.drawFull0.exit.firstOrError()
                    .map { copyCanvas() }
                    .observeOn(Schedulers.io())
                    .subscribeBy { saveImage(it) }
        }
    }
}