package org.runestar.client.api

import java.awt.event.ActionListener
import javax.swing.Icon

abstract class BarButton : Comparable<BarButton>, ActionListener {

    abstract val name: String

    abstract val icon: Icon

    override fun compareTo(other: BarButton): Int {
        return name.compareTo(other.name)
    }
}