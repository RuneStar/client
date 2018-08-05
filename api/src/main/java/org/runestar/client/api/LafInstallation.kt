package org.runestar.client.api

import com.alee.laf.WebLookAndFeel
import com.alee.skin.dark.DarkSkin
import org.runestar.client.game.api.Fonts
import javax.swing.JPopupMenu
import javax.swing.ToolTipManager
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

class LafInstallation : Runnable {

    override fun run() {
        WebLookAndFeel.install(DarkSkin::class.java)

        JPopupMenu.setDefaultLightWeightPopupEnabled(false)
        ToolTipManager.sharedInstance().isLightWeightPopupEnabled = false

        val chatFont = FontUIResource(Fonts.PLAIN_12)
        UIManager.getDefaults().entries.forEach { e ->
            val k = e.key as? String ?: return@forEach
            if (k.endsWith(".font")) {
                e.setValue(chatFont)
            }
        }

        System.setProperty("sun.awt.noerasebackground", "true")
    }
}