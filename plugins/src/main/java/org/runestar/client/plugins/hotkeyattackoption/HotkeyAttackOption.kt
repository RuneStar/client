package org.runestar.client.plugins.hotkeyattackoption

import org.runestar.client.api.forms.KeyStrokeForm
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.AttackOptionId
import org.runestar.client.api.game.live.AttackOptions
import org.runestar.client.api.game.live.Keyboard
import org.runestar.client.raw.access.XClient
import org.runestar.client.api.plugins.PluginSettings

class HotkeyAttackOption : DisposablePlugin<HotkeyAttackOption.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Hotkey Attack Option"

    override fun onStart() {
        Keyboard.strokes
                .filter(settings.keyStroke.value::equals)
                .delay { XClient.doCycle.enter }
                .subscribe {
                    AttackOptions.player = if (AttackOptions.player == settings.playerAttackOption1) {
                        settings.playerAttackOption2
                    } else {
                        settings.playerAttackOption1
                    }
                }
                .add()
    }

    class Settings(
            val playerAttackOption1: Int = AttackOptionId.HIDDEN,
            val playerAttackOption2: Int = AttackOptionId.LEFT_CLICK_WHERE_AVAILABLE,
            val keyStroke: KeyStrokeForm = KeyStrokeForm("released CONTROL")
    ) : PluginSettings()
}