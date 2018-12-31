package org.runestar.client.plugins.hotkeyrenderself

import org.runestar.client.api.forms.KeyStrokeForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.plugins.spi.PluginSettings

class HotkeyRenderSelf : DisposablePlugin<HotkeyRenderSelf.Settings>() {

    override val name = "Hotkey Render-Self"

    override val defaultSettings = Settings()

    override fun onStart() {
        Keyboard.strokes
                .filter(settings.keyStroke.get()::equals)
                .delay { XClient.doCycle.enter }
                .subscribe { CLIENT.renderSelf = !CLIENT.renderSelf }
                .add()
    }

    class Settings(
            val keyStroke: KeyStrokeForm = KeyStrokeForm("released END")
    ) : PluginSettings()
}