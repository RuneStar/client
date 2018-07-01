package org.runestar.client.plugins.dev

import org.runestar.client.api.forms.RgbForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XUnderlayDefinition
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class GroundColor : DisposablePlugin<GroundColor.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        CLIENT.underlayDefinition_cached.clear()
        add(XUnderlayDefinition.init.enter.subscribe {
            it.instance.rgb = settings.color.get().rgb
        })
    }

    override fun stop() {
        super.stop()
        CLIENT.underlayDefinition_cached.clear()
    }

    class Settings(
            val color: RgbForm = RgbForm(Color.WHITE)
    ) : PluginSettings()
}