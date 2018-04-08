package org.runestar.client.api

import com.alee.managers.language.data.TooltipWay
import com.alee.managers.style.StyleId
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
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

    private val left = TreeSet<BarButton>()

    private val right = TreeSet<BarButton>()

    private val logger = getLogger()

    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        preferredSize = Dimension(0, HEIGHT)
        minimumSize = Dimension(0, HEIGHT)
        maximumSize = Dimension(Int.MAX_VALUE, HEIGHT)
        isVisible = false
        rebuild()
    }

    fun addLeft(button: BarButton) {
        if (button !in right && left.add(button)) {
            rebuild()
        } else {
            logger.info { "Cannot add $button, it is already present" }
        }
    }

    fun addRight(button: BarButton) {
        if (button !in left && right.add(button)) {
            rebuild()
        } else {
            logger.info { "Cannot add $button, it is already present" }
        }
    }

    fun removeLeft(button: BarButton) {
        if (left.remove(button)) {
            rebuild()
        } else {
            logger.info { "Cannot remove $button, it is not present" }
        }
    }

    fun removeRight(button: BarButton) {
        if (right.remove(button)) {
            rebuild()
        } else {
            logger.info { "Cannot remove $button, it is not present" }
        }
    }

    internal fun clear() {
        left.clear()
        right.clear()
        rebuild()
    }

    private fun rebuild() {
        removeAll()
        left.forEach {
            add(it.makeButton(TooltipWay.right))
        }
        add(Box.createGlue())
        right.forEach {
            add(it.makeButton(TooltipWay.left))
        }
        revalidate()
        repaint()
    }

    private fun BarButton.makeButton(tooltipWay: TooltipWay): JButton {
        return JButton(icon).apply {
            putClientProperty(StyleId.STYLE_PROPERTY, StyleId.buttonIconHover)
            addActionListener(this@makeButton)
//            TooltipManager.addTooltip(this, this@makeButton.name, tooltipWay) // todo
        }
    }
}