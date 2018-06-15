package org.runestar.client.api.forms

import java.awt.event.InputEvent
import java.awt.event.KeyEvent

enum class KeyModifierForm(
        val mask: Int,
        val keyCode: Int
) {
    SHIFT(InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT),
    CONTROL(InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_CONTROL),
    ALT(InputEvent.ALT_DOWN_MASK, KeyEvent.VK_ALT)
}