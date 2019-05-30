package org.runestar.client.plugins.hotkeywalkhere

import org.runestar.client.api.forms.KeyCodeForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.MenuOption
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.api.live.Menu
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.PluginSettings

class HotkeyWalkHere : DisposablePlugin<HotkeyWalkHere.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Hotkey Walk-Here"

    override fun onStart() {
        add(Menu.optionAdditions.filter { Keyboard.isKeyPressed(settings.keyCode.value) }.subscribe(::onMenuOptionAdded))
    }

    private fun onMenuOptionAdded(option: MenuOption) {
        when (option) {
            is MenuOption.WalkHere, is MenuOption.Cancel, is MenuOption.InComponent -> return
            else -> CLIENT.menuOptionsCount--
        }
    }

    class Settings(
            val keyCode: KeyCodeForm = KeyCodeForm("SHIFT")
    ) : PluginSettings()
}