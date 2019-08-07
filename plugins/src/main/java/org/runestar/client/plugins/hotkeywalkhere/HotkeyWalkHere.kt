package org.runestar.client.plugins.hotkeywalkhere

import org.runestar.client.api.forms.KeyCodeForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.MiniMenuOption
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.api.live.MiniMenu
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.PluginSettings

class HotkeyWalkHere : DisposablePlugin<HotkeyWalkHere.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Hotkey Walk-Here"

    override fun onStart() {
        add(MiniMenu.optionAdditions.filter { Keyboard.isKeyPressed(settings.keyCode.value) }.subscribe(::onMenuOptionAdded))
    }

    private fun onMenuOptionAdded(option: MiniMenuOption) {
        when (option) {
            is MiniMenuOption.WalkHere, is MiniMenuOption.Cancel, is MiniMenuOption.InComponent -> return
            else -> CLIENT.menuOptionsCount--
        }
    }

    class Settings(
            val keyCode: KeyCodeForm = KeyCodeForm("SHIFT")
    ) : PluginSettings()
}