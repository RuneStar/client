package com.runesuite.client.ext.swing

import java.awt.event.InputEvent

fun InputEvent.getModifiersExText(): String {
    return InputEvent.getModifiersExText(modifiersEx)
}
