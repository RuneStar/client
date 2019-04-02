package org.runestar.client.plugins.hotkeyattackoption

import org.runestar.client.api.forms.KeyStrokeForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.AttackOption
import org.runestar.client.game.api.live.AttackOptions
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.plugins.spi.PluginSettings

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
            val playerAttackOption1: AttackOption = AttackOption.HIDDEN,
            val playerAttackOption2: AttackOption = AttackOption.LEFT_CLICK_WHERE_AVAILABLE,
            val keyStroke: KeyStrokeForm = KeyStrokeForm("released CONTROL")
    ) : PluginSettings()
}