package org.runestar.client.plugins.hotkeyrenderself

import org.runestar.client.api.forms.KeyStrokeForm
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Keyboard
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XClient
import org.runestar.client.api.plugins.PluginSettings

class HotkeyRenderSelf : DisposablePlugin<HotkeyRenderSelf.Settings>() {

    override val name = "Hotkey Render-Self"

    override val defaultSettings = Settings()

    override fun onStart() {
        Keyboard.strokes
                .filter(settings.keyStroke.value::equals)
                .delay { XClient.doCycle.enter }
                .subscribe { CLIENT.renderSelf = !CLIENT.renderSelf }
                .add()
    }

    class Settings(
            val keyStroke: KeyStrokeForm = KeyStrokeForm("released END")
    ) : PluginSettings()
}