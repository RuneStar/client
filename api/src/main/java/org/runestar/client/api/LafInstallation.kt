package org.runestar.client.api

import com.alee.laf.WebLookAndFeel
import com.alee.skin.dark.DarkSkin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import javax.swing.JPopupMenu
import javax.swing.PopupFactory
import javax.swing.ToolTipManager
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

internal object LafInstallation : Runnable {

    override fun run() {
        val chatFont = FontUIResource(RUNESCAPE_CHAT_FONT)

        UIManager.put("Label.font", chatFont)
        UIManager.put("Button.font", chatFont)
        UIManager.put("ToggleButton.font", chatFont)
        UIManager.put("Tree.font", chatFont)
        UIManager.put("ComboBox.font", chatFont)
        UIManager.put("MenuItem.font", chatFont)

        WebLookAndFeel.install(DarkSkin::class.java)

        JPopupMenu.setDefaultLightWeightPopupEnabled(false)
        ToolTipManager.sharedInstance().isLightWeightPopupEnabled = false
    }
}