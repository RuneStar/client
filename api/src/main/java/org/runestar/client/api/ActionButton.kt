package org.runestar.client.api

import java.awt.event.ActionListener
import javax.swing.Icon

abstract class ActionButton : Comparable<ActionButton>, ActionListener {

    abstract val name: String

    abstract val icon: Icon

    override fun compareTo(other: ActionButton) = name.compareTo(other.name)
}