package com.runesuite.client

import com.alee.extended.button.WebSwitch
import com.alee.laf.button.WebButton
import com.alee.laf.label.WebLabel
import com.alee.laf.list.WebList
import com.alee.laf.list.WebListModel
import com.alee.laf.scroll.WebScrollPane
import com.runesuite.client.plugins.PluginHandle
import com.runesuite.client.plugins.PluginLoader
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

internal class PluginsWindow(private val pluginLoader: PluginLoader) : JFrame("Plugins") {

    private val currentPlugins = HashSet<PluginHandle>()

    private val pluginsListModel = WebListModel<PluginHandle>()

    private val currentBox = Box.createHorizontalBox()

    private var selectedPlugin: PluginHandle? = null
    private var selectedPluginSwitch: WebSwitch? = null

    private val timer = Timer(600, null)

    init {
        refreshPlugins()
        add(WebScrollPane(WebList(pluginsListModel).apply {
            selectionMode = DefaultListSelectionModel.SINGLE_SELECTION
            addListSelectionListener {
                if (it.valueIsAdjusting) return@addListSelectionListener
                val plugin = this.selectedValue as PluginHandle? ?: return@addListSelectionListener
                if (selectedPlugin === plugin) return@addListSelectionListener
                selectedPlugin = plugin
                fillCurrentBox(plugin)
            }
        }), BorderLayout.CENTER)
        add(currentBox, BorderLayout.SOUTH)
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
        size = Dimension(400, 700)
        isVisible = true
    }

    private fun fillCurrentBox(plugin: PluginHandle) {
        currentBox.apply {
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
        revalidate()
        repaint()
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
        pluginsListModel.clear()
        currentPlugins.clear()
        val sorted = pluginLoader.plugins.sortedBy { it.name }
        pluginsListModel.addElements(sorted)
        currentPlugins.addAll(sorted)
        val sp = selectedPlugin
        if (sp != null && sp !in currentPlugins) {
            selectedPlugin = null
            selectedPluginSwitch = null
            currentBox.removeAll()
            revalidate()
            repaint()
        }
    }
}