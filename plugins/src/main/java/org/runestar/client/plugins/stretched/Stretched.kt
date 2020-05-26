package org.runestar.client.plugins.stretched

import org.kxtra.swing.graphics.drawImage
import org.runestar.client.api.forms.InterpolationForm
import org.runestar.client.api.game.live.Mouse
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.plugins.PluginSettings
import org.runestar.client.common.JAV_CONFIG
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XGameShell
import org.runestar.client.raw.access.XRasterProvider
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.MouseEvent
import kotlin.math.max
import kotlin.math.roundToInt

class Stretched : DisposablePlugin<Settings>() {

    override val defaultSettings = Settings()

    override fun onStart() {
        CLIENT.renderCount = Int.MIN_VALUE
        CLIENT.isCanvasInvalid = true

        add(XRasterProvider.setComponent.enter.subscribe {
            CLIENT.canvas.size = Dimension(
                    it.instance.width.scale(settings.scale),
                    it.instance.height.scale(settings.scale)
            )
        })

        add(XRasterProvider.drawFull0.enter.subscribe {
            it.skipBody = true
            val g = it.arguments[0] as Graphics2D
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, settings.interpolation.renderingHintValue)
            g.drawImage(it.instance.image, 0, 0, CLIENT.canvas.width, CLIENT.canvas.height)
        })

        add(XGameShell.getFrameContentBounds.exit.subscribe {
            val b = it.returned
            val minSize = JAV_CONFIG.appletMinSize
            b.width = max(minSize.width, b.width.scale(settings.scaleInverse))
            b.height = max(minSize.height, b.height.scale(settings.scaleInverse))
        })

        add(Mouse.methods.subscribe {
            val e = it.arguments[0] as MouseEvent
            if (e !is ScaledMouseEvent) {
                it.arguments[0] = ScaledMouseEvent(e, settings.scaleInverse)
            }
        })
    }
}

private fun Int.scale(scale: Double) = (this * scale).roundToInt()

private class ScaledMouseEvent(e: MouseEvent, scale: Double) : MouseEvent(
        e.component,
        e.id,
        e.`when`,
        e.modifiersEx,
        e.x.scale(scale), e.y.scale(scale),
        e.xOnScreen, e.yOnScreen,
        e.clickCount,
        e.isPopupTrigger,
        e.button
)

data class Settings(
        val scale: Double = 2.0,
        val interpolation: InterpolationForm = InterpolationForm.NEAREST_NEIGHBOR
) : PluginSettings() {

    @Transient val scaleInverse = 1.0 / scale
}