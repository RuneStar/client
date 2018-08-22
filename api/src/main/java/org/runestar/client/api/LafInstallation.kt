package org.runestar.client.api

import com.alee.laf.WebLookAndFeel
import com.alee.painter.decoration.content.TextRasterization
import com.alee.skin.dark.DarkSkin
import java.awt.RenderingHints
import javax.swing.JPopupMenu
import javax.swing.ToolTipManager
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

class LafInstallation : Runnable {

    override fun run() {
        WebLookAndFeel.install(DarkSkin::class.java)

        JPopupMenu.setDefaultLightWeightPopupEnabled(false)
        ToolTipManager.sharedInstance().isLightWeightPopupEnabled = false

        val uiDefaults = UIManager.getDefaults()

        val plain12Resource = FontUIResource(Fonts.PLAIN_12)
        for (key in uiDefaults.keys()) {
            val prevFont = uiDefaults.getFont(key)
            if (prevFont != null) {
                uiDefaults[key] = plain12Resource
            }
        }

        uiDefaults.remove(RenderingHints.KEY_TEXT_ANTIALIASING)
        uiDefaults.remove(RenderingHints.KEY_TEXT_LCD_CONTRAST)

        TextRasterization.basic.renderingHints.clear()
        TextRasterization.subpixel.renderingHints.clear()

        System.setProperty("sun.awt.noerasebackground", "true")
    }
}