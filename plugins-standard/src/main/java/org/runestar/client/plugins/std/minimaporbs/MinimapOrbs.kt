package org.runestar.client.plugins.std.minimaporbs

import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.Prayer
import org.runestar.client.game.api.WidgetGroupId
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Prayers
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import java.awt.BasicStroke
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

    private var rapidHeal = false
    @Volatile private var hpTicks = -1
    @Volatile private var specTicks = -1
    @Volatile private var hpTickLimit = HP_TICKS

    override fun start() {
        super.start()

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

        val stroke = BasicStroke(settings.stroke)

        add(LiveCanvas.repaints.subscribe { g ->

            val rh = Prayers.isEnabled(Prayer.RAPID_HEAL)
            if (rh != rapidHeal) {
                hpTicks = -1 // todo: sometimes 1 tick off
                hpTickLimit = if (rh) {
                    HP_TICKS / 2
                } else {
                    HP_TICKS
                }
            }
            rapidHeal = rh

            g.stroke = stroke
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

            if (settings.hpRegen.enabled) {
                Widgets[WidgetGroupId.MinimapOrbs.hp_circle]?.shape?.let { hpRect ->
                    if (hpTicks <= 0) return@let
                    val hpPercent = hpTicks.toDouble() / hpTickLimit
                    g.color = settings.hpRegen.color.get()
                    g.draw(orbArc(hpRect, hpPercent))
                }
            }

            Widgets[WidgetGroupId.MinimapOrbs.spec_circle]?.shape?.let { specRect ->

                if (settings.specFill.enabled && Game.specialAttackEnabled) {
                    g.color = settings.specFill.color.get()
                    g.fillOval(
                            specRect.x,
                            specRect.y,
                            specRect.width,
                            specRect.height
                    )
                }

                if (settings.specRegen.enabled) {
                    if (specTicks <= 0) return@let
                    val specPercent = specTicks.toDouble() / SPEC_TICKS
                    g.color = settings.specRegen.color.get()
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
            val stroke: Float = 3f,
            val hpRegen: Element = Element(ColorForm(220, 0, 0)),
            val specRegen: Element = Element(ColorForm(0, 200, 200)),
            val specFill: Element = Element(ColorForm(255, 255, 255, 120))
    ) : PluginSettings() {

        data class Element(
                val color: ColorForm,
                val enabled: Boolean = true
        )
    }
}