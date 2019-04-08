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
        private val BLACK = ColorUIResource(Color.BLACK)
        private val DARK_GRAY = ColorUIResource(Color.DARK_GRAY)
        private val GRAY = ColorUIResource(Color.GRAY)
        private val LIGHT_GRAY = ColorUIResource(Color.LIGHT_GRAY)

        fun install() {
            MetalLookAndFeel.setCurrentTheme(StarTheme())
            UIManager.setLookAndFeel(MetalLookAndFeel())
        }
    }

    override fun getName() = "Star"

    override fun getBlack() = BLACK

    override fun getControlDarkShadow() = BLACK
    override fun getPrimaryControlDarkShadow() = BLACK

    override fun getControlShadow() = white
    override fun getPrimaryControlShadow() = white

    override fun getFocusColor() = white

    override fun getControlTextFont() = FONT
    override fun getUserTextFont() = FONT
    override fun getWindowTitleFont() = FONT
    override fun getMenuTextFont() = FONT
    override fun getSubTextFont() = FONT
    override fun getSystemTextFont() = FONT

    override fun getControlTextColor() = white
    override fun getHighlightedTextColor() = white
    override fun getInactiveControlTextColor() = white
    override fun getUserTextColor() = white
    override fun getInactiveSystemTextColor() = white
    override fun getSystemTextColor() = white

    override fun getTextHighlightColor() = GRAY

    override fun getMenuBackground() = DARK_GRAY
    override fun getMenuSelectedBackground() = DARK_GRAY
    override fun getSeparatorBackground() = DARK_GRAY
    override fun getWindowBackground() = DARK_GRAY
    override fun getWindowTitleBackground() = DARK_GRAY
    override fun getWindowTitleInactiveBackground() = DARK_GRAY

    override fun getAcceleratorForeground() = LIGHT_GRAY
    override fun getAcceleratorSelectedForeground() = LIGHT_GRAY
    override fun getMenuDisabledForeground() = LIGHT_GRAY
    override fun getMenuForeground() = LIGHT_GRAY
    override fun getMenuSelectedForeground() = LIGHT_GRAY
    override fun getSeparatorForeground() = LIGHT_GRAY
    override fun getWindowTitleForeground() = LIGHT_GRAY
    override fun getWindowTitleInactiveForeground() = LIGHT_GRAY

    override fun getControl() = DARK_GRAY
    override fun getControlDisabled() = LIGHT_GRAY
    override fun getControlHighlight() = white
    override fun getPrimaryControlHighlight() = white
    override fun getControlInfo() = white
    override fun getDesktopColor() = LIGHT_GRAY
    override fun getPrimaryControl() = white
    override fun getPrimaryControlInfo() = white

    override fun addCustomEntriesToTable(table: UIDefaults) {
        System.setProperty("sun.awt.noerasebackground", "true")
        JPopupMenu.setDefaultLightWeightPopupEnabled(false)
        ToolTipManager.sharedInstance().isLightWeightPopupEnabled = false

        super.addCustomEntriesToTable(table)

        table[RenderingHints.KEY_TEXT_ANTIALIASING] = null
        table[RenderingHints.KEY_TEXT_LCD_CONTRAST] = null

        table["Button.gradient"] = null
        table["Button.margin"] = InsetsUIResource(2, 2, 2, 2)
        table["CheckBox.gradient"] = null
        table["MenuItem.border"] = null
        table["ScrollBar.gradient"] = null
    }
}