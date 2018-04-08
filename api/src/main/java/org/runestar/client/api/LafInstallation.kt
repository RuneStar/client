package org.runestar.client.api

import com.alee.laf.WebLookAndFeel
import com.alee.managers.style.StyleManager
import com.alee.skin.dark.DarkSkin
import org.kxtra.slf4j.loggerfactory.getLogger
import org.runestar.general.fonts.RUNESCAPE_CHAT_BOLD_FONT
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

internal object LafInstallation : Runnable {

    private val logger = getLogger()

    override fun run() {
        val chatFont = FontUIResource(RUNESCAPE_CHAT_FONT)
        val boldFont = FontUIResource(RUNESCAPE_CHAT_BOLD_FONT)

        UIManager.put("Label.font", chatFont)
        UIManager.put("MenuItem.font", boldFont)

        StyleManager.setDefaultSkin(DarkSkin::class.java)
        if (!WebLookAndFeel.install()) {
            logger.info("Failed to install LAF")
        }
    }
}