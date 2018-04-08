package org.runestar.client.api

import com.alee.managers.style.StyleId
import com.alee.managers.tooltip.TooltipManager
import com.alee.managers.tooltip.WebCustomTooltip
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.kxtra.swing.component.windowAncestor
import java.awt.*
import java.util.*
import javax.swing.*

class SidePanel internal constructor() : JPanel(BorderLayout()) {

    companion object {
        const val PANEL_WIDTH = 225
        const val BAR_WIDTH = 24
        const val WIDTH = PANEL_WIDTH + BAR_WIDTH
    }

    private val tabs: SortedSet<TabButton> = TreeSet()

    private val buttons: SortedSet<BarButton> = TreeSet()

    internal val panel: JPanel

    private val buttonsBox: Box

    private val logger = getLogger()

    private var selectedTab: TabButton? = null

    init {
        panel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(PANEL_WIDTH, 0)
            minimumSize = preferredSize
            maximumSize = Dimension(PANEL_WIDTH, Int.MAX_VALUE)
            isVisible = false
        }
        buttonsBox = Box.createVerticalBox().apply {
            preferredSize = Dimension(BAR_WIDTH, 0)
            minimumSize = preferredSize
            maximumSize = Dimension(BAR_WIDTH, Int.MAX_VALUE)
        }
        add(
                Box.createHorizontalBox().apply {
                    add(buttonsBox)
                    add(panel)
                }
        )
    }

    fun add(tab: TabButton) {
        if (!tabs.add(tab)) {
            logger.info { "Cannot add $tab, it is already present" }
            return
        }
        rebuild()
    }

    fun add(button: BarButton) {
        if (!buttons.add(button)) {
            logger.info { "Cannot add $button, it is already present" }
            return
        }
        rebuild()
    }

    internal fun clear() {
        tabs.clear()
        buttons.clear()
        rebuild()
    }

    fun remove(tab: TabButton) {
        if (!tabs.remove(tab)) {
            logger.info { "Cannot remove $tab, it is not present" }
            return
        }
        rebuild()
    }

    fun remove(button: BarButton) {
        if (!buttons.remove(button)) {
            logger.info { "Cannot remove $button, it is not present" }
            return
        }
        rebuild()
    }

    private fun rebuild() {
        buttonsBox.removeAll()
        panel.removeAll()
        tabs.forEach {
            buttonsBox.add(it.makeButton())
        }
        buttonsBox.add(Box.createGlue())
        buttons.forEach {
            buttonsBox.add(it.makeButton())
        }
        if (selectedTab == null) {
            selectedTab = tabs.firstOrNull()
        }
        selectedTab?.component?.let {
            panel.add(it)
        }
        revalidate()
        repaint()
    }

    private fun TabButton.makeButton(): Component {
        return JButton(icon).apply {
            preferredSize = maximumSize
            putClientProperty(StyleId.STYLE_PROPERTY, StyleId.buttonIcon)
//            TooltipManager.addTooltip(this, this@makeButton.name) // todo
            addActionListener {
                if (selectedTab != this@makeButton || !panel.isVisible) {
                    selectedTab = this@makeButton
                    panel.removeAll()
                    panel.add(component)
                    if (!panel.isVisible) {
                        panel.isVisible = true
                        (windowAncestor() as GameFrame).refit() // todo
                    }
                    panel.revalidate()
                    panel.repaint()
                }
            }
        }
    }

    private fun BarButton.makeButton(): Component {
        return JButton(icon).apply {
            preferredSize = maximumSize
            putClientProperty(StyleId.STYLE_PROPERTY, StyleId.buttonIconHover)
//            TooltipManager.addTooltip(this, this@makeButton.name) // todo
            addActionListener(this@makeButton)
        }
    }
}