package org.runestar.client.api

import com.alee.managers.style.StyleId
import com.alee.managers.tooltip.TooltipManager
import com.alee.managers.tooltip.TooltipWay
import org.kxtra.slf4j.info
import org.kxtra.slf4j.getLogger
import java.awt.Dimension
import java.util.*
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel

class TopBar internal constructor(): JPanel() {

    companion object {
        const val HEIGHT = 24
    }

    private val buttons = TreeSet<BarButton>()

    private val logger = getLogger()

    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        preferredSize = Dimension(0, HEIGHT)
        minimumSize = Dimension(0, HEIGHT)
        maximumSize = Dimension(Int.MAX_VALUE, HEIGHT)
        isVisible = false
        rebuild()
    }

    fun add(button: BarButton) {
        if (buttons.add(button)) {
            rebuild()
        } else {
            logger.info { "Cannot add $button, it is already present" }
        }
    }

    fun remove(button: BarButton) {
        if (buttons.remove(button)) {
            rebuild()
        } else {
            logger.info { "Cannot remove $button, it is not present" }
        }
    }

    internal fun clear() {
        buttons.clear()
        rebuild()
    }

    private fun rebuild() {
        removeAll()
        buttons.forEach {
            add(it.makeButton())
        }
        add(Box.createGlue())
        revalidate()
        repaint()
    }

    private fun BarButton.makeButton(): JButton {
        return JButton(icon).apply {
            putClientProperty(StyleId.STYLE_PROPERTY, StyleId.buttonIconHover)
            addActionListener(this@makeButton)
            TooltipManager.addTooltip(this, this@makeButton.name, TooltipWay.right) // todo
        }
    }
}