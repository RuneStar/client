package org.runestar.client.api

import java.awt.event.ActionEvent
import javax.imageio.ImageIO
import javax.swing.ImageIcon

class HideSidePanelButton : BarButton() {

    override val name = "Hide panel"

    override val icon = ImageIcon(ImageIO.read(javaClass.getResource("arrow-left.png")))

    override fun actionPerformed(e: ActionEvent) {
        val p = Application.frame.sidePanel.panel
        p.isVisible = !p.isVisible
        Application.frame.refit()
    }
}