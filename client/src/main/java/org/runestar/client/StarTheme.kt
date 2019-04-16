package org.runestar.client

import org.runestar.client.api.Fonts
import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import java.awt.Insets
import java.awt.Rectangle
import java.awt.RenderingHints
import javax.swing.AbstractButton
import javax.swing.JComponent
import javax.swing.JPopupMenu
import javax.swing.ToolTipManager
import javax.swing.UIDefaults
import javax.swing.UIManager
import javax.swing.border.AbstractBorder
import javax.swing.plaf.BorderUIResource
import javax.swing.plaf.ColorUIResource
import javax.swing.plaf.FontUIResource
import javax.swing.plaf.InsetsUIResource
import javax.swing.plaf.basic.BasicBorders
import javax.swing.plaf.metal.MetalButtonUI
import javax.swing.plaf.metal.MetalLookAndFeel
import javax.swing.plaf.metal.OceanTheme

class StarTheme : OceanTheme() {

    companion object {
        private val FONT = FontUIResource(Fonts.PLAIN_12)
        private val BACKGROUND = ColorUIResource(Color(0x3c3f41))
        private val FOREGROUND = ColorUIResource(Color(0xcccccc))
        private val INTERIOR = ColorUIResource(Color(0x45494a))
        private val INTERIOR2 = ColorUIResource(Color(0x535759))
        private val HIGHLIGHT = ColorUIResource(Color(0x292b2d))

        fun install() {
            MetalLookAndFeel.setCurrentTheme(StarTheme())
            UIManager.setLookAndFeel(MetalLookAndFeel())
        }
    }

    override fun getName() = "Star"

    override fun getControlTextFont() = FONT
    override fun getUserTextFont() = FONT
    override fun getWindowTitleFont() = FONT
    override fun getMenuTextFont() = FONT
    override fun getSubTextFont() = FONT
    override fun getSystemTextFont() = FONT

    override fun getBlack() = HIGHLIGHT
    override fun getWhite() = FOREGROUND

    override fun getControlDarkShadow() = HIGHLIGHT
    override fun getPrimaryControlDarkShadow() = HIGHLIGHT

    override fun getControlShadow() = INTERIOR
    override fun getPrimaryControlShadow() = INTERIOR

    override fun getControlHighlight() = HIGHLIGHT
    override fun getPrimaryControlHighlight() = HIGHLIGHT

    override fun getControlTextColor() = FOREGROUND
    override fun getHighlightedTextColor() = FOREGROUND
    override fun getInactiveControlTextColor() = FOREGROUND
    override fun getUserTextColor() = FOREGROUND
    override fun getInactiveSystemTextColor() = FOREGROUND
    override fun getSystemTextColor() = FOREGROUND

    override fun getFocusColor() = HIGHLIGHT
    override fun getTextHighlightColor() = HIGHLIGHT
    override fun getMenuBackground() = BACKGROUND
    override fun getMenuSelectedBackground() = INTERIOR
    override fun getSeparatorBackground() = BACKGROUND
    override fun getWindowBackground() = INTERIOR
    override fun getWindowTitleBackground() = BACKGROUND
    override fun getWindowTitleInactiveBackground() = BACKGROUND
    override fun getAcceleratorForeground() = FOREGROUND
    override fun getAcceleratorSelectedForeground() = FOREGROUND
    override fun getMenuDisabledForeground() = FOREGROUND
    override fun getMenuForeground() = FOREGROUND
    override fun getMenuSelectedForeground() = FOREGROUND
    override fun getSeparatorForeground() = FOREGROUND
    override fun getWindowTitleForeground() = FOREGROUND
    override fun getWindowTitleInactiveForeground() = FOREGROUND
    override fun getControl() = BACKGROUND
    override fun getControlDisabled() = FOREGROUND
    override fun getControlInfo() = FOREGROUND
    override fun getDesktopColor() = FOREGROUND
    override fun getPrimaryControl() = FOREGROUND
    override fun getPrimaryControlInfo() = HIGHLIGHT

    override fun addCustomEntriesToTable(table: UIDefaults) {
        System.setProperty("sun.awt.noerasebackground", "true")
        JPopupMenu.setDefaultLightWeightPopupEnabled(false)
        ToolTipManager.sharedInstance().isLightWeightPopupEnabled = false

        super.addCustomEntriesToTable(table)

        table[RenderingHints.KEY_TEXT_ANTIALIASING] = null
        table[RenderingHints.KEY_TEXT_LCD_CONTRAST] = null

        table["Button.border"] = BorderUIResource.CompoundBorderUIResource(ButtonBorder, BasicBorders.MarginBorder())
        table["Button.gradient"] = null
        table["Button.margin"] = InsetsUIResource(2, 2, 2, 2)
        table["Button.select"] = INTERIOR2
        table["CheckBox.background"] = INTERIOR
        table["CheckBox.border"] = null
        table["CheckBox.gradient"] = null
        table["MenuItem.border"] = null
        table["ScrollBar.background"] = INTERIOR
        table["ScrollBar.gradient"] = null
        table["ScrollBar.thumb"] = BACKGROUND

        table["ButtonUI"] = ButtonUI::class.java.name
    }

    private object ButtonBorder : AbstractBorder() {

        override fun paintBorder(c: Component, g: Graphics, x: Int, y: Int, w: Int, h: Int) {
            val button = c as AbstractButton
            g.translate(x, y)
            g.color = MetalLookAndFeel.getControlDarkShadow()
            g.drawRect(0, 0, w - 1, h - 1)
            if (button.model.isRollover || button.model.isPressed || button.hasFocus()) {
                g.drawRect(1, 1, w - 3, h - 3)
            }
        }

        override fun getBorderInsets(c: Component, insets: Insets) = insets.apply { set(3, 3, 3, 3) }
    }

    object ButtonUI : MetalButtonUI() {

        @JvmStatic fun createUI(c: JComponent) = this

        override fun paint(g: Graphics, c: JComponent) {
            val button = c as AbstractButton
            if (button.isEnabled && button.isContentAreaFilled && !button.isBorderPainted && (button.model.isRollover || (button.isFocusPainted && button.hasFocus()))) {
                val size = button.size
                g.color = MetalLookAndFeel.getControlShadow()
                g.fillRect(0, 0, size.width, size.height)
            }
            super.paint(g, c)
        }

        override fun paintFocus(g: Graphics, b: AbstractButton, viewRect: Rectangle?, textRect: Rectangle?, iconRect: Rectangle?) {}
    }
}