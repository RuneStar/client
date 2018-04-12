package org.runestar.client.api

import com.alee.laf.WebLookAndFeel
import com.alee.skin.dark.DarkSkin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import javax.swing.JPopupMenu
import javax.swing.ToolTipManager
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

internal object LafInstallation : Runnable {

    override fun run() {
        WebLookAndFeel.install(DarkSkin::class.java)

        JPopupMenu.setDefaultLightWeightPopupEnabled(false)
        ToolTipManager.sharedInstance().isLightWeightPopupEnabled = false

        val chatFont = FontUIResource(RUNESCAPE_CHAT_FONT)
        UIManager.getDefaults().entries.forEach { e ->
            val k = e.key as? String ?: return@forEach
            if (k.endsWith(".font")) {
                e.setValue(chatFont)
            }
        }
    }
}