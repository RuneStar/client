package com.runesuite.client.ext.swing

import java.awt.Component
import java.awt.Window
import javax.swing.SwingUtilities

fun Component.getWindowAncestor(): Window? {
    return SwingUtilities.getWindowAncestor(this)
}