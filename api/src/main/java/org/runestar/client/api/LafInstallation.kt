package org.runestar.client.api

import com.alee.laf.WebLookAndFeel
import com.alee.skin.dark.DarkSkin
import org.runestar.general.fonts.RUNESCAPE_CHAT_BOLD_FONT
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import javax.swing.JPopupMenu
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

internal object LafInstallation : Runnable {

    override fun run() {
        val chatFont = FontUIResource(RUNESCAPE_CHAT_FONT)
        val boldFont = FontUIResource(RUNESCAPE_CHAT_BOLD_FONT)

        UIManager.put("Label.font", chatFont)
        UIManager.put("MenuItem.font", boldFont)

        WebLookAndFeel.install(DarkSkin::class.java)

        JPopupMenu.setDefaultLightWeightPopupEnabled(false)
    }
}