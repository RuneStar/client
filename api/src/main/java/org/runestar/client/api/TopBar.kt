package org.runestar.client.api

import com.alee.managers.language.data.TooltipWay
import com.alee.managers.style.StyleId
import com.alee.managers.tooltip.TooltipManager
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.*
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.ToolTipManager

class TopBar internal constructor(): JPanel(BorderLayout()) {

    companion object {
        const val HEIGHT = 30
    }

    private val left = TreeSet<TopBarButton>()

    private val right = TreeSet<TopBarButton>()

    private val box = Box.createHorizontalBox()

    private val logger = getLogger()

    init {
        box.apply {
            preferredSize = Dimension(0, HEIGHT)
            minimumSize = preferredSize
            maximumSize = Dimension(Int.MAX_VALUE, HEIGHT)
        }
        add(box)
        rebuild()
    }

    fun addLeft(button: TopBarButton) {
        if (button !in right && left.add(button)) {
            rebuild()
        } else {
            logger.info { "Cannot add $button, it is already present" }
        }
    }

    fun addRight(button: TopBarButton) {
        if (button !in left && right.add(button)) {
            rebuild()
        } else {
            logger.info { "Cannot add $button, it is already present" }
        }
    }

    fun removeLeft(button: TopBarButton) {
        if (left.remove(button)) {
            rebuild()
        } else {
            logger.info { "Cannot remove $button, it is not present" }
        }
    }

    fun removeRight(button: TopBarButton) {
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
        box.removeAll()
        left.forEach {
            box.add(it.makeButton(TooltipWay.right))
        }
        box.add(Box.createGlue())
        right.forEach {
            box.add(it.makeButton(TooltipWay.left))
        }
        revalidate()
        repaint()
    }

    private fun TopBarButton.makeButton(tooltipWay: TooltipWay): JButton {
        return JButton(icon).apply {
            putClientProperty(StyleId.STYLE_PROPERTY, StyleId.buttonIconHover)
            addActionListener(this@makeButton)
            TooltipManager.addTooltip(this, this@makeButton.name, tooltipWay)
        }
    }
}