package org.runestar.client.api

import java.awt.event.ActionListener
import javax.swing.Icon

abstract class TopBarButton : Comparable<TopBarButton>, ActionListener {

    abstract val name: String

    abstract val icon: Icon

    override fun compareTo(other: TopBarButton): Int {
        return name.compareTo(other.name)
    }
}