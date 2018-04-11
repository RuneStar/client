package org.runestar.client.plugins.screenshot

import org.runestar.client.api.Application
import org.runestar.client.api.BarButton
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XRasterProvider
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.TrayIcon
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
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

    companion object {
        const val IMAGE_FILE_EXTENSION = "png"
        const val SCREENSHOTS_DIRECTORY_NAME = "screenshots"
    }

    override val defaultSettings = Settings()

    lateinit var timeFormatter: DateTimeFormatter
    lateinit var screenshotDirectory: Path

    private val button = Button()

    override fun start() {
        timeFormatter = createTimeFormatter()
        screenshotDirectory = ctx.directory.resolve(SCREENSHOTS_DIRECTORY_NAME)

        add(Keyboard.events
                .filter { it.extendedKeyCode == KeyEvent.VK_PRINTSCREEN && it.id == KeyEvent.KEY_RELEASED }
                .flatMapSingle { XRasterProvider.drawFull0.exit.firstOrError() }
                .map { it.instance.image as BufferedImage }
                .subscribe { takeScreenshot(it) }
        )
        Application.frame.topBar.add(button)
    }

    override fun stop() {
        super.stop()
        Application.frame.topBar.remove(button)
    }

    fun createTimeFormatter(): DateTimeFormatter {
        val zoneId = if (ctx.settings.localizeTimeZone) ZoneId.systemDefault() else ZoneId.from(ZoneOffset.UTC)
        return DateTimeFormatter.ofPattern(ctx.settings.dateTimeFormatterPattern)
                .withZone(zoneId)
    }

    fun takeScreenshot(img: RenderedImage) {
        val rsn = Client.accessor.localPlayerName
        val timeString = timeFormatter.format(Instant.now())
        val fileName = "$rsn.$timeString.${IMAGE_FILE_EXTENSION}"
        val path = screenshotDirectory.resolve(fileName)
        try {
            Files.createDirectories(path)
            ImageIO.write(img, IMAGE_FILE_EXTENSION, path.toFile())
            if (ctx.settings.trayNotify) {
                Application.trayIcon.displayMessage(
                        "Screenshot Taken",
                        fileName,
                        TrayIcon.MessageType.NONE
                )
            }
        } catch (e: IOException) {
            ctx.logger.error("Failed to take screenshot", e)
        }
    }

    data class Settings(
            val dateTimeFormatterPattern: String = "yyyy-MM-dd'T'kk-mm-ss,SSS",
            val localizeTimeZone: Boolean = true,
            val trayNotify: Boolean = true
    ) : PluginSettings()

    inner class Button : BarButton() {

        override val name: String = "Screenshot"

        override val icon: Icon = ImageIcon(ImageIO.read(javaClass.getResource("camera.png")))

        override fun actionPerformed(e: ActionEvent) {
            val me = XRasterProvider.drawFull0.exit.firstOrError().blockingGet()
            val img = me.instance.image as BufferedImage
            takeScreenshot(img)
        }
    }
}