package org.runestar.client.plugins.dev.interfaceexplorer

import org.runestar.client.api.game.Component

data class ComponentWrapper(val component: Component) {

    override fun toString(): String {
        return component.idString()
    }
}