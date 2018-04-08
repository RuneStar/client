package org.runestar.client.api

import com.alee.managers.style.StyleId
import org.runestar.client.common.ICON
import org.runestar.client.plugins.PluginHandle
import org.runestar.client.plugins.PluginLoader
import java.awt.Component
import javax.swing.*

class PluginsTab(val pluginLoader: PluginLoader) : SidePanelTab(){

    override val name = "Plugins"

    override val icon = ImageIcon(ICON)

    override val component: JScrollPane

    private val timer = Timer(600, null)

    private var curPlugins: MutableMap<PluginHandle, Boolean>

    private val pluginsBox: Box

    init {
        pluginsBox = Box.createVerticalBox()
        component = JScrollPane(
                pluginsBox,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        ).apply {
            verticalScrollBar.unitIncrement = 16
        }
        curPlugins = pluginLoader.snapshot()
        refresh()
        timer.apply {
            addActionListener {
                if (component.isShowing) {
                    tryRefresh()
                }
            }
            start()
        }
    }

    private fun tryRefresh() {
        val ps = pluginLoader.snapshot()
        if (ps != curPlugins) {
            curPlugins = ps
            refresh()
        }
    }

    private fun refresh() {
        pluginsBox.removeAll()
        curPlugins.keys.forEach {
            pluginsBox.add(it.createComponent())
        }
        pluginsBox.revalidate()
        pluginsBox.repaint()
    }

    private fun PluginHandle.createComponent(): Component {
        val popup = JPopupMenu().apply {
            add(JMenuItem("Settings").apply {
                addActionListener { desktop?.safeOpen(settingsFile) }
            })
            add(JMenuItem("Directory").apply {
                addActionListener { desktop?.safeOpen(directory) }
            })
        }
        return Box.createHorizontalBox().apply {
            add(Box.createHorizontalStrut(3))
            add(JLabel(this@createComponent.toString()).apply {
                putClientProperty(StyleId.STYLE_PROPERTY, StyleId.labelShade)
            })
            add(Box.createGlue())
            add(JCheckBox().apply {
                isSelected = isRunning
                addActionListener {
                    if (isSelected) this@createComponent.start() else this@createComponent.stop()
                    curPlugins[this@createComponent] = isSelected
                }
            })
            add(JButton("...").apply {
                putClientProperty(StyleId.STYLE_PROPERTY, StyleId.buttonHover)
                addActionListener {
                    popup.show(this, 0, bounds.height)
                }
            })
            add(Box.createHorizontalStrut(3))
        }
    }

    private fun PluginLoader.snapshot(): MutableMap<PluginHandle, Boolean> {
        return plugins.associateTo(LinkedHashMap()) { it to it.isRunning }
    }
}