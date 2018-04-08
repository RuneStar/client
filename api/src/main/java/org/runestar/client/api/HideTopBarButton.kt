package org.runestar.client.api

import org.runestar.client.common.ICON
import java.awt.event.ActionEvent
import javax.imageio.ImageIO
import javax.swing.Icon
import javax.swing.ImageIcon

class HideTopBarButton : BarButton() {

    override val name = "Hide top bar"

    override val icon = ImageIcon(ImageIO.read(javaClass.getResource("arrow-up.png")))

    override fun actionPerformed(e: ActionEvent) {
        val b = Application.frame.topBar
        b.isVisible = !b.isVisible
        Application.frame.refit()
    }
}