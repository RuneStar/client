package org.runestar.client.api

import java.awt.Color
import java.awt.RenderingHints
import javax.swing.JPopupMenu
import javax.swing.ToolTipManager
import javax.swing.UIDefaults
import javax.swing.UIManager
import javax.swing.plaf.ColorUIResource
import javax.swing.plaf.FontUIResource
import javax.swing.plaf.InsetsUIResource
import javax.swing.plaf.metal.MetalLookAndFeel
import javax.swing.plaf.metal.OceanTheme

class StarTheme : OceanTheme() {

    companion object {
        private val FONT = FontUIResource(Fonts.PLAIN_12)
        private val BACKGROUND = ColorUIResource(Color(0x3c3f41))
        private val FOREGROUND = ColorUIResource(Color(0xcccccc))
        private val INTERIOR = ColorUIResource(Color(0x45494a))
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
    override fun getPrimaryControlShadow() = BACKGROUND
    override fun getFocusColor() = HIGHLIGHT
    override fun getControlTextColor() = FOREGROUND
    override fun getHighlightedTextColor() = FOREGROUND
    override fun getInactiveControlTextColor() = FOREGROUND
    override fun getUserTextColor() = FOREGROUND
    override fun getInactiveSystemTextColor() = FOREGROUND
    override fun getSystemTextColor() = FOREGROUND
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
    override fun getControlHighlight() = HIGHLIGHT
    override fun getPrimaryControlHighlight() = HIGHLIGHT
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

        table["Button.gradient"] = null
        table["Button.margin"] = InsetsUIResource(2, 2, 2, 2)
        table["CheckBox.background"] = INTERIOR
        table["CheckBox.gradient"] = null
        table["CheckBox.border"] = null
        table["MenuItem.border"] = null
        table["ScrollBar.background"] = INTERIOR
        table["ScrollBar.gradient"] = null
    }
}