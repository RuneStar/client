package org.runestar.client.plugins.minimaporbs

import org.runestar.client.api.forms.BasicStrokeForm
import org.runestar.client.api.forms.RgbaForm
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.cacheids.PrayerId
import org.runestar.client.cacheids.StatId
import org.runestar.client.api.game.GameState
import org.runestar.client.api.game.ComponentId
import org.runestar.client.api.game.live.Game
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.game.live.Prayers
import org.runestar.client.api.game.live.Stats
import org.runestar.client.api.game.live.Components
import org.runestar.client.api.plugins.PluginSettings
import java.awt.RenderingHints
import java.awt.Shape
import java.awt.geom.Arc2D
import java.awt.geom.Rectangle2D

class MinimapOrbs : DisposablePlugin<MinimapOrbs.Settings>() {

    private companion object {
        const val SPEC_TICKS = 50
        const val HP_TICKS = 100
    }

    override val defaultSettings = Settings()

    override val name = "Minimap Orbs"

    private var rapidHeal = false
    private var hpTicks = -1
    private var specTicks = -1
    private var hpTickLimit = HP_TICKS

    override fun onStart() {
        add(Game.stateChanges.subscribe { state ->
            if (state == GameState.LOGGING_IN || state == GameState.CHANGING_WORLD) {
                specTicks = -1
                hpTicks = -1
            }
        })

        add(Game.ticks.subscribe {
            specTicks = if (Game.specialAttackPercent == 100) {
                -1
            } else {
                (specTicks + 1) % SPEC_TICKS
            }
            hpTicks = (hpTicks + 1) % hpTickLimit
        })

        add(Canvas.repaints.subscribe { g ->

            val rh = Prayers.enabled[PrayerId.RAPID_HEAL]
            if (rh != rapidHeal) {
                hpTicks = -1 // todo: sometimes 1 tick off
                hpTickLimit = if (rh) {
                    HP_TICKS / 2
                } else {
                    HP_TICKS
                }
            }
            rapidHeal = rh

            g.stroke = settings.stroke.value
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

            if (settings.hpRegen.enabled && hpTicks > 0) {
                Components[ComponentId.MINIMAP_ORBS_HP_CIRCLE]?.shape?.let { hpRect ->
                    val boost = Stats.boost(StatId.HITPOINTS)
                    val hpPercent = when {
                        boost < 0 -> hpTicks.toDouble() / hpTickLimit
                        boost > 0 -> 1 - (hpTicks.toDouble() / hpTickLimit)
                        else -> return@let
                    }
                    g.color = settings.hpRegen.color.value
                    g.draw(orbArc(hpRect, hpPercent))
                }
            }

            Components[ComponentId.MINIMAP_ORBS_SPEC_CIRCLE]?.shape?.let { specRect ->

                if (settings.specFill.enabled && Game.specialAttackEnabled) {
                    g.color = settings.specFill.color.value
                    g.fillOval(
                            specRect.x,
                            specRect.y,
                            specRect.width,
                            specRect.height
                    )
                }

                if (settings.specRegen.enabled && specTicks > 0) {
                    val specPercent = specTicks.toDouble() / SPEC_TICKS
                    g.color = settings.specRegen.color.value
                    g.draw(orbArc(specRect, specPercent))
                }
            }
        })
    }

    private fun orbArc(bounds: Rectangle2D, percent: Double): Shape {
        return Arc2D.Double(
                bounds,
                90.0,
                -360.0 * percent,
                Arc2D.OPEN
        )
    }

    data class Settings(
            val stroke: BasicStrokeForm = BasicStrokeForm(2f),
            val hpRegen: Element = Element(RgbaForm(220, 0, 0)),
            val specRegen: Element = Element(RgbaForm(0, 200, 200)),
            val specFill: Element = Element(RgbaForm(255, 255, 255, 120))
    ) : PluginSettings() {

        data class Element(
                val color: RgbaForm,
                val enabled: Boolean = true
        )
    }
}