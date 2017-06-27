package com.runesuite.client.ext.swing

import java.awt.event.KeyEvent

fun KeyEvent.getKeyText(): String {
    return KeyEvent.getKeyText(keyCode)
}