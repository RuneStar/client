package org.runestar.client.plugins.notes

import org.runestar.client.api.Application
import org.runestar.client.api.TabButton
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.plugins.PluginSettings
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.SwingUtilities

class Notes : DisposablePlugin<Notes.Settings>() {

    override val defaultSettings = Settings()

    override fun onStart() {
        SwingUtilities.invokeLater { add(Application.frame.register(Button())) }
    }

    inner class Button : TabButton() {

        override val name = this@Notes.name

        override val icon = ImageIcon(ImageIO.read(javaClass.getResource("pencil-on-page.png")))

        override val component = JScrollPane(
                JTextArea(settings.text).apply {
                    addFocusListener(object : FocusAdapter() {
                        override fun focusLost(e: FocusEvent) {
                            if (settings.text == text) return
                            synchronized(settings) {
                                settings.text = text
                            }
                            settings.write()
                        }
                    })
                }
        )
    }

    class Settings(
            var text: String = ""
    ) : PluginSettings()
}