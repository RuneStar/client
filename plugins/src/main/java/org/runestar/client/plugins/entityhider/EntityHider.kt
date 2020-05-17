package org.runestar.client.plugins.entityhider

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.raw.CLIENT
import org.runestar.client.api.plugins.PluginSettings
import org.runestar.client.raw.access.XActor
import org.runestar.client.raw.access.XClient
import org.runestar.client.raw.access.XNpc
import org.runestar.client.raw.access.XPlayer
import org.runestar.client.raw.access.XScene

class EntityHider : DisposablePlugin<EntityHider.Settings>() {

    override val name = "Entity Hider"

    override val defaultSettings = Settings()

    override fun onStart() {
        add(XScene.newScenery.enter.subscribe {
            val actor = it.arguments[8] as? XActor? ?: return@subscribe
            if (shouldHide(actor)) {
                it.returned = false
                it.skipBody = true
            }
        })
        add(XClient.drawActor2d.enter.subscribe {
            val actor = it.arguments[0] as XActor
            if (shouldHide(actor)) {
                it.skipBody = true
            }
        })
    }

    private fun shouldHide(actor: XActor): Boolean {
        return (settings.hideNpcs && actor is XNpc) ||
                (settings.hidePlayers && actor is XPlayer && actor != CLIENT.localPlayer) ||
                (settings.hideSelf && actor == CLIENT.localPlayer)
    }

    class Settings(
            val hideNpcs: Boolean = true,
            val hidePlayers: Boolean = true,
            val hideSelf: Boolean = false
    ) : PluginSettings()
}