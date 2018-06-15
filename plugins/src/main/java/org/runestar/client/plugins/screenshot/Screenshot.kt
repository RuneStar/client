package org.runestar.client.plugins.screenshot

import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.runestar.client.api.Application
import org.runestar.client.api.BarButton
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XRasterProvider
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.TrayIcon
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.image.*
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

        add(Keyboard.events
                .filter { it.keyCode == ctx.settings.keyCode && it.id == KeyEvent.KEY_RELEASED }
                .delay { XRasterProvider.drawFull0.exit }
                .map { copyCanvas() }
                .observeOn(Schedulers.io())
                .subscribe { saveImage(it) })

        Application.frame.topBar.add(button)
    }

    override fun stop() {
        super.stop()
        Application.frame.topBar.remove(button)
    }

    private fun createTimeFormatter(): DateTimeFormatter {
        val zoneId = if (ctx.settings.localizeTimeZone) ZoneId.systemDefault() else ZoneId.from(ZoneOffset.UTC)
        return DateTimeFormatter.ofPattern(ctx.settings.dateTimeFormatterPattern)
                .withZone(zoneId)
    }

    private fun copyCanvas(): BufferedImage {
        val rasterProvider = Client.accessor.rasterProvider
        val pixelsCopy = rasterProvider.pixels.copyOf()
        val w = rasterProvider.width
        val h = rasterProvider.height
        val buf = DataBufferInt(pixelsCopy, pixelsCopy.size)
        val colorModel = DirectColorModel(32, 16711680, 65280, 255)
        val writableRaster = Raster.createWritableRaster(colorModel.createCompatibleSampleModel(w, h), buf, null)
        return BufferedImage(colorModel, writableRaster, false, null)
    }

    private fun saveImage(img: RenderedImage) {
        val rsn = Client.accessor.localPlayerName
        val timeString = timeFormatter.format(Instant.now())
        val fileName = "$rsn.$timeString.$IMAGE_FILE_EXTENSION"
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
            val keyCode: Int = KeyEvent.VK_PRINTSCREEN,
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