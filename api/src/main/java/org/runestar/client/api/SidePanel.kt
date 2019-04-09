package org.runestar.client.api

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

internal class SidePanel : JPanel() {

    companion object {
        const val PANEL_WIDTH = 225
        const val BAR_WIDTH = 24
    }

    private val tabs: SortedSet<TabButton> = TreeSet()

    private val buttons: SortedSet<ActionButton> = TreeSet()

    private var firstTabs: SortedSet<TabButton> = TreeSet()

    internal val panel: JPanel

    private val buttonsBox: Box

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
        require(tabs.add(tab))
        rebuild()
    }

    fun addFirst(tab: TabButton) {
        require(firstTabs.add(tab))
        rebuild()
    }

    fun add(button: ActionButton) {
        require(buttons.add(button))
        rebuild()
    }

    fun clear() {
        firstTabs.clear()
        tabs.clear()
        buttons.clear()
        rebuild()
    }

    fun remove(tab: TabButton) {
        require(tabs.remove(tab))
        rebuild()
    }

    internal fun removeFirst(tab: TabButton) {
        require(firstTabs.remove(tab))
        rebuild()
    }

    internal fun remove(button: ActionButton) {
        require(buttons.remove(button))
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
        if (selectedTab != null && selectedTab !in tabs && selectedTab !in firstTabs) {
            selectedTab = firstTabs.firstOrNull() ?: tabs.firstOrNull()
        }
        selectedTab?.let {
            panel.add(it.component, BorderLayout.CENTER)
        }
        revalidate()
        repaint()
    }

    private fun TabButton.makeButton(): Component {
        return JButton(icon).apply {
            isFocusable = false
            isBorderPainted = false
            toolTipText = this@makeButton.name
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

    private fun ActionButton.makeButton(): Component {
        return JButton(icon).apply {
            isFocusable = false
            isBorderPainted = false
            toolTipText = this@makeButton.name
            alignmentY = JComponent.BOTTOM_ALIGNMENT
            alignmentX = JComponent.CENTER_ALIGNMENT
            addActionListener(this@makeButton)
        }
    }
}