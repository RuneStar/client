package org.runestar.client.plugins.attackstyles

import org.runestar.client.api.Fonts
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.BR_TAG
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.ScriptId
import org.runestar.client.game.api.VarbitId
import org.runestar.client.game.api.VarpId
import org.runestar.client.game.api.ComponentId
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Components
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.access.XClientScriptEvent
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D

class AttackStyles : DisposablePlugin<AttackStyles.Settings>() {

    private companion object {
        const val PADDING_X = 3
        const val PADDING_TOP = 2
        const val PADDING_BOTTOM = 1
        val OUTLINE_COLOR = Color(14, 13, 15)
        val FILL_COLOR = Color(70, 61, 50, 156)
        val TEXT_COLOR: Color = Color.WHITE
        val TEXT_COLOR_WARN: Color = Color.RED
    }

    override val name = "Attack Styles"

    override val defaultSettings = Settings()

    private var weaponType = -1

    private var attackStyleName: String? = null

    private var warn = false

    override fun onStart() {
        add(XClient.doCycleLoggedIn.exit.subscribe { onCycle() })
        add(XClient.runClientScript0.exit.map { it.arguments[0] as XClientScriptEvent }.subscribe(::onRunScript))
        add(LiveCanvas.repaints.subscribe(::onDraw))
    }

    override fun onStop() {
        weaponType = -1
        attackStyleName = null
        unhide()
    }

    private fun onCycle() {
        if (CLIENT.getVarbit(VarbitId.WEAPON_TYPE) == weaponType) return
        val scriptEvent = CLIENT._ClientScriptEvent_()
        scriptEvent.setArgs(arrayOf<Any>(ScriptId.COMBAT_INTERFACE_SETUP))
        CLIENT.runClientScript(scriptEvent)
    }

    private fun onRunScript(scriptEvent: XClientScriptEvent) {
        val scriptId = scriptEvent.args0[0] as? Int ?: return
        if (scriptId != ScriptId.COMBAT_INTERFACE_SETUP) return
        weaponType = CLIENT.getVarbit(VarbitId.WEAPON_TYPE)
        val style = Game.varps[VarpId.ATTACK_STYLE]
        val desc = if (style == 4) {
            if (Game.getVarbit(VarbitId.DEFENSIVE_CASTING_MODE) == 1) {
                "(Defensive Casting)$BR_TAG(Magic XP)$BR_TAG(Defence XP)"
            } else {
                "(Casting)$BR_TAG(Magic XP)"
            }
        } else {
            CLIENT.interpreter_stringLocals[4 + style]
        }
        hide()
        warn = isHidden(desc)
        attackStyleName = desc.split(BR_TAG, limit = 2)[0].removePrefix("(").removeSuffix(")")
    }

    private fun onDraw(g: Graphics2D) {
        if (!settings.alwaysShow && !warn) return
        if (Game.state != GameState.LOGGED_IN) return
        val s = attackStyleName ?: return
        val chatBoxRect = Components[ComponentId.CHAT_BOX]?.shape ?: return
        g.font = Fonts.PLAIN_12
        val height = g.fontMetrics.height + PADDING_TOP + PADDING_BOTTOM
        val width = g.fontMetrics.stringWidth(s) + PADDING_X * 2
        val x = chatBoxRect.maxX.toInt() - width - 9
        val y = chatBoxRect.y - height - 6
        g.color = FILL_COLOR
        g.fillRect(x, y, width, height)
        g.color = OUTLINE_COLOR
        g.drawRect(x, y, width, height)
        g.color = if (warn) TEXT_COLOR_WARN else TEXT_COLOR
        g.drawString(s, x + PADDING_X, y + PADDING_TOP + g.fontMetrics.ascent)
    }

    private fun hide() {
        if (isHidden(CLIENT.interpreter_stringLocals[4 + 0])) {
            Components[ComponentId.COMBAT_OPTIONS_STYLE_1]?.isHidden = true
        }
        if (isHidden(CLIENT.interpreter_stringLocals[4 + 1])) {
            Components[ComponentId.COMBAT_OPTIONS_STYLE_2]?.isHidden = true
        }
        if (isHidden(CLIENT.interpreter_stringLocals[4 + 2])) {
            Components[ComponentId.COMBAT_OPTIONS_STYLE_3]?.isHidden = true
        }
        if (isHidden(CLIENT.interpreter_stringLocals[4 + 3])) {
            Components[ComponentId.COMBAT_OPTIONS_STYLE_4]?.isHidden = true
        }
        if (settings.hideMagic) {
            Components[ComponentId.COMBAT_OPTIONS_STYLE_SPELL]?.isHidden = true
            Components[ComponentId.COMBAT_OPTIONS_STYLE_SPELL_2]?.isHidden = true
        }
        if (settings.hideMagic || settings.hideDefence) {
            Components[ComponentId.COMBAT_OPTIONS_STYLE_DEFENSIVE_SPELL]?.isHidden = true
            Components[ComponentId.COMBAT_OPTIONS_STYLE_DEFENSIVE_SPELL_2]?.isHidden = true
        }
    }

    private fun unhide() {
        Components[ComponentId.COMBAT_OPTIONS_STYLE_1]?.isHidden = false
        Components[ComponentId.COMBAT_OPTIONS_STYLE_2]?.isHidden = false
        Components[ComponentId.COMBAT_OPTIONS_STYLE_3]?.isHidden = false
        Components[ComponentId.COMBAT_OPTIONS_STYLE_4]?.isHidden = false
        Components[ComponentId.COMBAT_OPTIONS_STYLE_SPELL]?.isHidden = false
        Components[ComponentId.COMBAT_OPTIONS_STYLE_SPELL_2]?.isHidden = false
        Components[ComponentId.COMBAT_OPTIONS_STYLE_DEFENSIVE_SPELL]?.isHidden = false
        Components[ComponentId.COMBAT_OPTIONS_STYLE_DEFENSIVE_SPELL_2]?.isHidden = false
    }

    private fun isHidden(desc: String): Boolean {
        if (settings.hideAttack && ("Attack XP" in desc || "Shared XP" in desc)) return true
        if (settings.hideStrength && ("Strength XP" in desc || "Shared XP" in desc)) return true
        if (settings.hideDefence && ("Defence XP" in desc || "Shared XP" in desc)) return true
        if (settings.hideRanged && "Ranged XP" in desc) return true
        if (settings.hideMagic && "Magic XP" in desc) return true
        return false
    }

    class Settings(
            val alwaysShow: Boolean = true,
            val hideAttack: Boolean = false,
            val hideStrength: Boolean = false,
            val hideDefence: Boolean = false,
            val hideMagic: Boolean = false,
            val hideRanged: Boolean = false
    ) : PluginSettings()
}