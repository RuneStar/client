package org.runestar.client.api

import java.awt.Component
import javax.swing.Icon

abstract class SidePanelTab : Comparable<SidePanelTab> {

    abstract val name: String

    abstract val icon: Icon

    abstract val component: Component

    override fun compareTo(other: SidePanelTab): Int {
        return name.compareTo(other.name)
    }
}