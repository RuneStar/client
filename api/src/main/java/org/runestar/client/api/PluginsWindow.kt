package org.runestar.client.api

import org.kxtra.slf4j.loggerfactory.getLogger
import org.runestar.client.common.ICON
import org.runestar.client.plugins.PluginHandle
import org.runestar.client.plugins.PluginLoader
import java.awt.Window
import java.awt.event.ItemEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.*
import javax.swing.*
import javax.swing.Timer
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener

internal class PluginsWindow(private val pluginLoader: PluginLoader) : JFrame("Plugins") {

    private val timer = Timer(600, null)

    private val checkBox = JCheckBox("enabled")

    private var currentPlugin: PluginHandle? = null

    private val openButton = JButton("open...")

    private val comboModelVector = Vector<PluginHandle>()

    val logger = getLogger()

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
            add(JComboBox<PluginHandle>(DefaultComboBoxModel(comboModelVector)).apply {
                model.selectedItem = null
                maximumRowCount *= 2
                addPopupMenuListener(object : PopupMenuListener {
                    override fun popupMenuCanceled(e: PopupMenuEvent) {}
                    override fun popupMenuWillBecomeInvisible(e: PopupMenuEvent) {}
                    override fun popupMenuWillBecomeVisible(e: PopupMenuEvent) {
                        model = DefaultComboBoxModel(comboModelVector)
                        model.selectedItem = currentPlugin
                    }
                })
                addItemListener {
                    logger.info(it.toString())
                    if (it.stateChange != ItemEvent.SELECTED) return@addItemListener
                    if (it.item == currentPlugin) return@addItemListener
                    logger.info(it.item.toString())
                    currentPlugin = it.item as PluginHandle
                    checkBox.isEnabled = true
                    openButton.isEnabled = true
                }
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
        comboModelVector.clear()
        comboModelVector.addAll(pluginLoader.plugins.sortedBy { it.name })
    }
}