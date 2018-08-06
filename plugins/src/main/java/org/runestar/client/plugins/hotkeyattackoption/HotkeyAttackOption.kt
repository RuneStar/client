package org.runestar.client.plugins.hotkeyattackoption

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.AttackOption
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XAttackOption
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.event.KeyEvent

class HotkeyAttackOption : DisposablePlugin<HotkeyAttackOption.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Hotkey Attack Option"

    private var normalPlayerAttackOption: XAttackOption? = null

    override fun start() {
        XClient.doCycleLoggedIn.enter.subscribe {
            if (Keyboard.isKeyPressed(settings.keyCode)) {
                if (CLIENT.playerAttackOption != settings.playerAttackOption.accessor) {
                    normalPlayerAttackOption = CLIENT.playerAttackOption
                    CLIENT.playerAttackOption = settings.playerAttackOption.accessor
                }
            } else if (normalPlayerAttackOption != null && CLIENT.playerAttackOption == settings.playerAttackOption.accessor) {
                CLIENT.playerAttackOption = normalPlayerAttackOption
                normalPlayerAttackOption = null
            }
        }
    }

    class Settings(
            val playerAttackOption: AttackOption = AttackOption.LEFT_CLICK_WHERE_AVAILABLE,
            val keyCode: Int = KeyEvent.VK_CONTROL
    ) : PluginSettings()
}