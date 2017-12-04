package com.runesuite.client

import com.alee.extended.button.WebSwitch
import com.alee.laf.button.WebButton
import com.alee.laf.label.WebLabel
import com.alee.laf.list.WebList
import com.alee.laf.list.WebListModel
import com.alee.laf.panel.WebPanel
import com.alee.laf.scroll.WebScrollPane
import com.alee.laf.splitpane.WebSplitPane
import com.runesuite.client.plugins.PluginHandle
import com.runesuite.client.plugins.PluginLoader
import org.kxtra.slf4j.loggerfactory.getLogger
import java.awt.BorderLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

internal class PluginsWindow(private val pluginLoader: PluginLoader) : JFrame("Plugins") {

    private companion object {
        val logger = getLogger()
    }

    private val currentPlugins = HashSet<PluginHandle>()

    private val pluginsListModel = WebListModel<PluginHandle>()

    private val splitPane = WebSplitPane(WebSplitPane.HORIZONTAL_SPLIT).apply {
        dividerSize = 2
    }

    private val rightPanel = WebPanel(BorderLayout())

    private val rightTopPanel = WebPanel().apply {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
    }

    private val rightBodyPanel = WebPanel()

    private var selectedPlugin: PluginHandle? = null
    private var selectedPluginSwitch: WebSwitch? = null

    private val timer = Timer(600, null)

    init {
        refreshPlugins()
        rightPanel.apply {
            add(rightTopPanel, BorderLayout.NORTH)
            add(WebScrollPane(rightBodyPanel), BorderLayout.CENTER)
        }
        splitPane.leftComponent = WebScrollPane(WebList(pluginsListModel).apply {
            selectionMode = DefaultListSelectionModel.SINGLE_SELECTION
            addListSelectionListener {
                if (it.valueIsAdjusting) return@addListSelectionListener
                val plugin = this.selectedValue as PluginHandle? ?: return@addListSelectionListener
                selectedPlugin = plugin
                rightTopPanel.removeAll()
                fillPluginTopPanel(plugin)
            }
        })
        splitPane.rightComponent = rightPanel
        add(splitPane)
        pack()
        timer.apply {
            addActionListener { refresh() }
            start()
        }
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                timer.stop()
            }
        })
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        isVisible = true
    }

    private fun fillPluginTopPanel(plugin: PluginHandle) {
        rightTopPanel.apply {
            removeAll()
            val switch = WebSwitch().apply {
                if (plugin.isDestroyed) isEnabled = false else isSelected = plugin.isEnabled
                maximumSize = preferredSize
                addActionListener {
                    if (this.isSelected) plugin.enable() else plugin.disable()
                }
            }
            add(switch)
            selectedPluginSwitch = switch
            add(Box.createGlue())
            add(WebLabel(plugin.name.substringAfterLast('.')))
            add(Box.createGlue())
            add(WebButton("settings").apply {
                addActionListener { openFile(plugin.settingsFile) }
            })
            add(WebButton("log").apply {
                addActionListener { openFile(plugin.logFile) }
            })
            add(WebButton("directory").apply {
                addActionListener { openFile(plugin.directory) }
            })
        }
    }

    private fun refresh() {
        if (pluginLoader.plugins.size != currentPlugins.size || !currentPlugins.containsAll(pluginLoader.plugins)) {
            refreshPlugins()
        }
        val switch = selectedPluginSwitch
        val plugin = selectedPlugin
        if (switch != null && plugin != null) {
            if (plugin.isDestroyed && switch.isEnabled) {
                switch.isEnabled = false
                revalidate()
                repaint()
            } else if (switch.isSelected != plugin.isEnabled) {
                switch.isSelected = plugin.isEnabled
                revalidate()
                repaint()
            }
        }
    }

    private fun refreshPlugins() {
        logger.info("refreshing plugins")
        pluginsListModel.clear()
        currentPlugins.clear()
        val sorted = pluginLoader.plugins.sortedBy { it.name }
        pluginsListModel.addElements(sorted)
        currentPlugins.addAll(sorted)
        if (selectedPlugin !in currentPlugins) {
            selectedPlugin = null
            selectedPluginSwitch = null
            rightTopPanel.removeAll()
            revalidate()
            repaint()
        }
    }
}