package org.runestar.client.plugins.shiftclickwalkhere

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.MenuOption
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.api.live.Menu
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.event.KeyEvent

class ShiftClickWalkHere : DisposablePlugin<ShiftClickWalkHere.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Shift-click Walk-here"

    override fun start() {
        add(Menu.optionAdditions.filter { Keyboard.isKeyPressed(ctx.settings.keyCode) }.subscribe(::onMenuOptionAdded))
    }

    private fun onMenuOptionAdded(option: MenuOption) {
        when (option) {
            is MenuOption.WalkHere, is MenuOption.Cancel, is MenuOption.InWidget -> return
            else -> Client.accessor.menuOptionsCount--
        }
    }

    class Settings(
            val keyCode: Int = KeyEvent.VK_SHIFT
    ) : PluginSettings()
}