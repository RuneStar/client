package org.runestar.client.api

import org.runestar.client.common.ICON
import org.runestar.client.plugins.PluginHandle
import org.runestar.client.plugins.PluginLoader
import java.awt.Window
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener

internal class PluginsWindow(private val pluginLoader: PluginLoader) : JFrame("Plugins") {

    private val timer = Timer(600, null)

    private val checkBox = JCheckBox("enabled")

    private var currentPlugin: PluginHandle? = null

    private val openButton = JButton("open...")

    private val comboModel = DefaultComboBoxModel<PluginHandle>()

    init {
        iconImage = ICON
        val popup = JPopupMenu().apply {
            add(JMenuItem("settings").apply {
                addActionListener { currentPlugin?.let { openFile(it.settingsFile) } }
            })
            add(JMenuItem("log").apply {
                addActionListener { currentPlugin?.let { openFile(it.logFile) } }
            })
            add(JMenuItem("directory").apply {
                addActionListener { currentPlugin?.let { openFile(it.directory) } }
            })
        }
        add(Box.createHorizontalBox().apply {
            add(checkBox.apply {
                isEnabled = false
                addActionListener {
                    val cp = currentPlugin
                    if (cp != null) {
                        if (isSelected) cp.start() else cp.stop()
                    }
                }
            })
            refreshPlugins()
            add(JComboBox<PluginHandle>(comboModel).apply {
                model.selectedItem = null
                maximumRowCount *= 2
                addPopupMenuListener(object : PopupMenuListener {
                    override fun popupMenuCanceled(e: PopupMenuEvent) {}
                    override fun popupMenuWillBecomeInvisible(e: PopupMenuEvent) {
                        currentPlugin = selectedItem as PluginHandle?
                        if (currentPlugin != null) {
                            checkBox.isEnabled = true
                            openButton.isEnabled = true
                        }
                    }
                    override fun popupMenuWillBecomeVisible(e: PopupMenuEvent) {
                        refreshPlugins()
                    }
                })
            })
            add(openButton.apply {
                isEnabled = false
                addActionListener {
                    popup.show(this, 0, bounds.height)
                }
            })
        })
        timer.apply {
            addActionListener {
                val cp = currentPlugin
                if (cp != null && checkBox.isSelected != cp.isRunning) {
                    checkBox.isSelected = cp.isRunning
                    revalidate()
                    repaint()
                }
            }
            start()
        }
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                timer.stop()
            }
        })
        type = Window.Type.UTILITY
        pack()
        isResizable = false
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        setLocationRelativeTo(Application.frame)
        isVisible = true
    }

    private fun refreshPlugins() {
        val selected = comboModel.selectedItem
        comboModel.removeAllElements()
        pluginLoader.plugins.sortedBy { it.name }.forEach { comboModel.addElement(it) }
        comboModel.selectedItem = selected
    }
}