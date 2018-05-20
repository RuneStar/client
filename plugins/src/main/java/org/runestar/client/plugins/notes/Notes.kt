package org.runestar.client.plugins.notes

import com.alee.managers.style.StyleId
import org.runestar.client.api.Application
import org.runestar.client.api.TabButton
import org.runestar.client.api.TopBar
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginContext
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Component
import java.awt.Dimension
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*

class Notes : AbstractPlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    private val panel by lazy { Panel() }

    private lateinit var file: File

    override fun init(ctx: PluginContext<PluginSettings>) {
        super.init(ctx)
        file = ctx.directory.resolve("notes.txt").toFile()
    }

    override fun start() {
        Application.frame.sidePanel.add(panel)
    }

    override fun stop() {
        Application.frame.sidePanel.remove(panel)
    }

    inner class Panel : TabButton() {

        override val name = "Notes"

        override val icon = ImageIcon(ImageIO.read(javaClass.getResource("pencil-on-page.png")))

        override val component: Component

        private fun read(): String {
            if (file.createNewFile()) return ""
            return file.inputStream().use {
                it.bufferedReader(Charsets.UTF_8).use {
                    it.readText()
                }
            }
        }

        private fun write(s: String) {
            file.outputStream().use {
                it.bufferedWriter(Charsets.UTF_8).use {
                    it.write(s)
                }
            }
        }

        init {
            component = Box.createVerticalBox().apply {
                add(
                        JLabel("Notes").apply {
                            alignmentX = Component.CENTER_ALIGNMENT
                            preferredSize = Dimension(0, TopBar.HEIGHT)
                            putClientProperty(
                                    StyleId.STYLE_PROPERTY,
                                    StyleId.labelShadow
                            )
                        }
                )
                add(
                        JScrollPane(
                                JTextArea(read()).apply {
                                    addFocusListener(object : FocusAdapter() {
                                        override fun focusLost(e: FocusEvent) {
                                            write(text)
                                        }
                                    })
                                }
                        ).apply {
                            verticalScrollBar.apply {
                                putClientProperty(
                                        StyleId.STYLE_PROPERTY,
                                        StyleId.scrollbarUndecoratedButtonless
                                )
                            }
                            horizontalScrollBar.apply {
                                putClientProperty(
                                        StyleId.STYLE_PROPERTY,
                                        StyleId.scrollbarUndecoratedButtonless
                                )
                            }
                        }
                )
            }
        }
    }
}