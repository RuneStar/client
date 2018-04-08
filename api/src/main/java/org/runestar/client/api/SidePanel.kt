package org.runestar.client.api

import com.alee.managers.style.StyleId
import com.alee.managers.tooltip.TooltipManager
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.util.*
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JPanel

class SidePanel internal constructor() : JPanel(BorderLayout()) {

    companion object {
        const val PANEL_WIDTH = 225
        const val BAR_WIDTH = 30
        const val WIDTH = PANEL_WIDTH + BAR_WIDTH
    }

    private val tabs: SortedSet<SidePanelTab> = TreeSet()

    private val panel: JPanel

    private val buttonsBox: Box

    private val logger = getLogger()

    private var selectedTab: SidePanelTab? = null

    init {
        panel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(PANEL_WIDTH, 0)
            minimumSize = preferredSize
            maximumSize = Dimension(PANEL_WIDTH, Int.MAX_VALUE)
        }
        buttonsBox = Box.createVerticalBox().apply {
            preferredSize = Dimension(BAR_WIDTH, 0)
            minimumSize = preferredSize
            maximumSize = Dimension(BAR_WIDTH, Int.MAX_VALUE)
        }
        add(
                Box.createHorizontalBox().apply {
                    add(panel)
                    add(buttonsBox)
                }
        )
    }

    fun add(tab: SidePanelTab) {
        if (!tabs.add(tab)) {
            logger.info { "Cannot add $tab, it is already present" }
            return
        }
        rebuild()
    }

    internal fun clear() {
        tabs.clear()
        rebuild()
    }

    fun remove(tab: SidePanelTab) {
        if (!tabs.remove(tab)) {
            logger.info { "Cannot remove $tab, it is not present" }
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
        if (selectedTab == null) {
            selectedTab = tabs.firstOrNull()
        }
        selectedTab?.let {
            panel.add(it.component)
        }
        revalidate()
        repaint()
    }

    private fun SidePanelTab.makeButton(): Component {
        return JButton(icon).apply {
            putClientProperty(StyleId.STYLE_PROPERTY, StyleId.buttonIcon)
            TooltipManager.addTooltip(this, this@makeButton.name)
            addActionListener {
                if (selectedTab != this@makeButton) {
                    selectedTab = this@makeButton
                    panel.removeAll()
                    panel.add(component)
                    panel.revalidate()
                    panel.repaint()
                }
            }
        }
    }
}