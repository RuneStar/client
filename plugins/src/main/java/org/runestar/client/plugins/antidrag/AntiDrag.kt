package org.runestar.client.plugins.antidrag

import org.runestar.client.api.forms.KeyCodeForm
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Keyboard
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XClient
import org.runestar.client.api.plugins.PluginSettings

class AntiDrag : DisposablePlugin<AntiDrag.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Anti-Drag"

    private var dragDurationBuffer = 0

    override fun onStart() {
        add(XClient.doCycle.enter.filter { Keyboard.isKeyPressed(settings.keyCode.value) }.subscribe { onCycle() })
    }

    private fun onCycle() {
        if (dragDurationBuffer >= settings.delay) {
            dragDurationBuffer = 0
            return
        }
        if (CLIENT.itemDragDuration == 1) {
            dragDurationBuffer++
            CLIENT.itemDragDuration = 0
        }
    }

    class Settings(
            val keyCode: KeyCodeForm = KeyCodeForm("CONTROL"),
            val delay: Int = 25
    ) : PluginSettings()
}