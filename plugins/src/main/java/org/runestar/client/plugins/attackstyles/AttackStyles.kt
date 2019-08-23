package org.runestar.client.plugins.attackstyles

import org.runestar.client.api.Fonts
import org.runestar.client.api.forms.FontForm
import org.runestar.client.api.forms.InsetsForm
import org.runestar.client.api.overlay.Anchor
import org.runestar.client.api.overlay.HideableOverlay
import org.runestar.client.api.overlay.TextOverlay
import org.runestar.client.api.overlay.hideable
import org.runestar.client.api.overlay.withBackground
import org.runestar.client.api.overlay.withBorder
import org.runestar.client.api.overlay.withPadding
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.cacheids.ScriptId
import org.runestar.client.game.api.BR_TAG
import org.runestar.client.game.api.ComponentId
import org.runestar.client.game.api.VarbitId
import org.runestar.client.game.api.VarpId
import org.runestar.client.game.api.live.Components
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.access.XClientScriptEvent
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class AttackStyles : DisposablePlugin<AttackStyles.Settings>() {

    override val name = "Attack Styles"

    override val defaultSettings = Settings()

    private var weaponType = -1

    private lateinit var text: TextOverlay

    private lateinit var overlay: HideableOverlay

    override fun onStart() {
        text = TextOverlay("Style", settings.font.value, Color.WHITE)
        overlay = text
                .withPadding(settings.padding)
                .withBackground()
                .withBorder()
                .hideable()
        overlay.show = settings.alwaysShow
        add(XClient.doCycleLoggedIn.exit.subscribe { onCycle() })
        add(XClient.runClientScript0.exit.map { it.arguments[0] as XClientScriptEvent }.subscribe(::onRunScript))
        add(settings.anchor.add(overlay))
    }

    override fun onStop() {
        weaponType = -1
        unhide()
    }

    private fun onCycle() {
        if (CLIENT.getVarbit(VarbitId.WEAPON_TYPE) == weaponType) return
        val scriptEvent = CLIENT._ClientScriptEvent_()
        scriptEvent.setArgs(arrayOf<Any>(ScriptId.CLIENTSCRIPT_COMBAT_INTERFACE_SETUP))
        CLIENT.runClientScript(scriptEvent)
    }

    private fun onRunScript(scriptEvent: XClientScriptEvent) {
        val scriptId = scriptEvent.args0[0] as? Int ?: return
        if (scriptId != ScriptId.CLIENTSCRIPT_COMBAT_INTERFACE_SETUP) return
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
        val warn = isHidden(desc)
        text.color = if (warn) Color.RED else Color.WHITE
        overlay.show = warn || settings.alwaysShow
        text.modified = true
        text.string = desc.split(BR_TAG, limit = 2)[0].removePrefix("(").removeSuffix(")")
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
            val hideRanged: Boolean = false,
            val anchor: Anchor = Anchor.BOTTOM_RIGHT,
            val font: FontForm = FontForm(Fonts.PLAIN_12),
            val padding: InsetsForm = InsetsForm(1, 2, 1, 2)
    ) : PluginSettings()
}