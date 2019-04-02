package org.runestar.client.plugins.dev

import org.runestar.client.api.forms.RgbForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class SkyboxColor : DisposablePlugin<SkyboxColor.Settings>() {

    override val defaultSettings = Settings()

    override fun onStart() {
        add(XScene.draw.enter.subscribe {
            CLIENT.Rasterizer2D_fillRectangle(
                    CLIENT.viewportOffsetX, CLIENT.viewportOffsetY,
                    CLIENT.viewportWidth, CLIENT.viewportHeight,
                    settings.color.value.rgb
            )
        })
    }

    class Settings(
            val color: RgbForm = RgbForm(Color.CYAN)
    ) : PluginSettings()
}