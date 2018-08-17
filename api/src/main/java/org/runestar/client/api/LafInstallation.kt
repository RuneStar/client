package org.runestar.client.api

import com.alee.laf.WebLookAndFeel
import com.alee.skin.dark.DarkSkin
import javax.swing.JPopupMenu
import javax.swing.ToolTipManager
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

class LafInstallation : Runnable {

    override fun run() {
        WebLookAndFeel.install(DarkSkin::class.java)

        JPopupMenu.setDefaultLightWeightPopupEnabled(false)
        ToolTipManager.sharedInstance().isLightWeightPopupEnabled = false

        val plain12Resource = FontUIResource(Fonts.PLAIN_12)
        val uiDefaults = UIManager.getDefaults()
        for (key in uiDefaults.keys()) {
            val prevFont = uiDefaults.getFont(key)
            if (prevFont != null) {
                uiDefaults[key] = plain12Resource
            }
        }

        System.setProperty("sun.awt.noerasebackground", "true")
    }
}