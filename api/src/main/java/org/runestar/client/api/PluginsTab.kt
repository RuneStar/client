package org.runestar.client.api

import com.alee.managers.style.StyleId
import hu.akarnokd.rxjava2.swing.SwingSchedulers
import org.runestar.client.api.util.desktop
import org.runestar.client.api.util.safeOpen
import org.runestar.client.plugins.spi.PluginLoader
import java.awt.Component
import javax.imageio.ImageIO
import javax.swing.*

class PluginsTab(val plugins: Collection<PluginLoader.Holder<*>>) : TabButton() {

    override val name = "Plugins"

    override val icon = ImageIcon(ImageIO.read(javaClass.getResource("cog.png")))

    override val component: JScrollPane

    private val pluginsBox = JPanel().apply { layout = BoxLayout(this, BoxLayout.Y_AXIS) }

    init {
        component = JScrollPane(null).apply {
            setViewportView(pluginsBox)
            verticalScrollBar.apply {
                unitIncrement = 16
                putClientProperty(StyleId.STYLE_PROPERTY, StyleId.scrollbarUndecoratedButtonless)
            }
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        }
        plugins.forEach {
            pluginsBox.add(it.createComponent())
        }
    }

    private fun PluginLoader.Holder<*>.createComponent(): Component {
        val popup = JPopupMenu().apply {
            add(JMenuItem("Settings").apply {
                addActionListener { desktop?.safeOpen(ctx.settingsFile) }
            })
            add(JMenuItem("Directory").apply {
                addActionListener { desktop?.safeOpen(ctx.directory) }
            })
        }
        return Box.createHorizontalBox().apply {
            add(Box.createHorizontalStrut(3))
            add(JLabel(this@createComponent.toString()).apply {
                putClientProperty(StyleId.STYLE_PROPERTY, StyleId.labelShadow)
            })
            add(Box.createGlue())
            add(JCheckBox().apply {
                isSelected = false
                isRunningChanged.subscribeOn(SwingSchedulers.edt()).subscribe {
                    isSelected = it
                }
                addActionListener {
                    setIsRunning(isSelected)
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
}