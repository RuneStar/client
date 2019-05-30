package org.runestar.client.plugins.dev.interfaceexplorer

import org.runestar.client.game.api.Component

data class ComponentWrapper(val component: Component) {

    override fun toString(): String {
        return component.idString()
    }
}