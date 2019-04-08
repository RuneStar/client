package org.runestar.client.api

import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.kxtra.swing.component.window
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.util.SortedSet
import java.util.TreeSet
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

class SidePanel internal constructor() : JPanel() {

    companion object {
        const val PANEL_WIDTH = 225
        const val BAR_WIDTH = 24
        const val WIDTH = PANEL_WIDTH + BAR_WIDTH
    }

    private val tabs: SortedSet<TabButton> = TreeSet()

    private val buttons: SortedSet<BarButton> = TreeSet()

    private var firstTabs: SortedSet<TabButton> = TreeSet()

    internal val panel: JPanel

    private val buttonsBox: Box

    private val logger = getLogger()

    private var selectedTab: TabButton? = null

    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        panel = JPanel(BorderLayout()).apply {
            alignmentX = JComponent.RIGHT_ALIGNMENT
            alignmentY = JComponent.CENTER_ALIGNMENT
            preferredSize = Dimension(PANEL_WIDTH, 0)
            minimumSize = Dimension(PANEL_WIDTH, 0)
            maximumSize = Dimension(PANEL_WIDTH, Int.MAX_VALUE)
            isVisible = false
        }
        buttonsBox = Box.createVerticalBox().apply {
            alignmentX = JComponent.LEFT_ALIGNMENT
            alignmentY = JComponent.CENTER_ALIGNMENT
            preferredSize = Dimension(BAR_WIDTH, 0)
            minimumSize = Dimension(BAR_WIDTH, 0)
            maximumSize = Dimension(BAR_WIDTH, Int.MAX_VALUE)
        }
        alignmentX = JComponent.CENTER_ALIGNMENT
        alignmentY = JComponent.CENTER_ALIGNMENT
        add(buttonsBox)
        add(panel)
    }

    fun add(tab: TabButton) {
        if (!tabs.add(tab)) {
            logger.info { "Cannot add $tab, it is already present" }
            return
        }
        rebuild()
    }

    internal fun addFirst(tab: TabButton) {
        if (!firstTabs.add(tab)) {
            logger.info { "Cannot add $tab, it is already present" }
            return
        }
        rebuild()
    }

    internal fun add(button: BarButton) {
        if (!buttons.add(button)) {
            logger.info { "Cannot add $button, it is already present" }
            return
        }
        rebuild()
    }

    internal fun clear() {
        firstTabs.clear()
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

    internal fun removeFirst(tab: TabButton) {
        if (!firstTabs.remove(tab)) {
            logger.info { "Cannot remove $tab, it is not present" }
            return
        }
        rebuild()
    }

    internal fun remove(button: BarButton) {
        if (!buttons.remove(button)) {
            logger.info { "Cannot remove $button, it is not present" }
            return
        }
        rebuild()
    }

    private fun rebuild() {
        buttonsBox.removeAll()
        panel.removeAll()
        firstTabs.forEach {
            buttonsBox.add(it.makeButton())
        }
        tabs.forEach {
            buttonsBox.add(it.makeButton())
        }
        buttonsBox.add(Box.createGlue())
        buttons.forEach {
            buttonsBox.add(it.makeButton())
        }
        if (selectedTab == null) {
            selectedTab = firstTabs.firstOrNull() ?: tabs.firstOrNull()
        }
        selectedTab?.component?.let {
            panel.add(it, BorderLayout.CENTER)
        }
        revalidate()
        repaint()
    }

    private fun TabButton.makeButton(): Component {
        return JButton(icon).apply {
            isContentAreaFilled = false
            alignmentX = JComponent.CENTER_ALIGNMENT
            addActionListener {
                if (selectedTab != this@makeButton || !panel.isVisible) {
                    val wasVisible = panel.isVisible
                    selectedTab = this@makeButton
                    panel.isVisible = false
                    panel.removeAll()
                    panel.add(component, BorderLayout.CENTER)
                    panel.isVisible = true
                    if (!wasVisible) {
                        (window as GameFrame).refit() // todo
                    } else {
                        panel.revalidate()
                        panel.repaint()
                    }
                } else {
                    panel.isVisible = false
                    (window as GameFrame).refit()
                }
            }
        }
    }

    private fun BarButton.makeButton(): Component {
        return JButton(icon).apply {
            isContentAreaFilled = false
            alignmentY = JComponent.BOTTOM_ALIGNMENT
            alignmentX = JComponent.CENTER_ALIGNMENT
            addActionListener(this@makeButton)
        }
    }
}