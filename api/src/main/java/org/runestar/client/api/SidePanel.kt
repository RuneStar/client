package org.runestar.client.api

import java.awt.BorderLayout
import java.awt.Dimension
import java.util.*
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JPanel

class SidePanel internal constructor() : JPanel(BorderLayout()) {

    private val panel: JPanel

    private val buttonsBox: Box

    private val tabs: SortedMap<SidePanelTab, JButton> = TreeMap()

    private var selectedTab: SidePanelTab? = null

    init {
        panel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(225, 0)
            border = BorderFactory.createEtchedBorder()
        }
        buttonsBox = Box.createVerticalBox().apply {
            add(Box.createGlue())
            preferredSize = Dimension(30, 0)
            border = BorderFactory.createEtchedBorder()
        }
        add(
                Box.createHorizontalBox().apply {
                    add(panel)
                    add(buttonsBox)
                }
        )
    }

    fun add(tab: SidePanelTab) {
        if (tabs.containsKey(tab)) return
        val button = tab.makeButton()
        button.addActionListener {
            if (selectedTab != tab) {
                selectedTab = tab
                panel.removeAll()
                panel.add(tab.component)
                panel.revalidate()
                panel.repaint()
            }
        }
        tabs[tab] = button
        buttonsBox.removeAll()
        tabs.forEach {
            buttonsBox.add(it.value)
        }
        buttonsBox.add(Box.createGlue())
        if (selectedTab == null) {
            selectedTab = tab
            panel.add(tab.component)
        }
        revalidate()
        repaint()
    }

    internal fun clear() {
        selectedTab = null
        tabs.clear()
        buttonsBox.removeAll()
        buttonsBox.add(Box.createGlue())
        panel.removeAll()
        revalidate()
        repaint()
    }

    fun remove(tab: SidePanelTab) {
        val button = tabs.remove(tab) ?: return
        if (selectedTab == tab) {
            selectedTab = null
            panel.removeAll()
        }
        buttonsBox.remove(button)
        revalidate()
        repaint()
    }

    private fun SidePanelTab.makeButton(): JButton {
        return JButton(icon).apply {
            isBorderPainted = false
            isFocusPainted = false
            toolTipText = name
        }
    }
}