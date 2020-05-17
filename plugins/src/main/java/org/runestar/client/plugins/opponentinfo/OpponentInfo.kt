package org.runestar.client.plugins.opponentinfo

import org.runestar.client.api.Fonts
import org.runestar.client.api.forms.FontForm
import org.runestar.client.api.forms.RgbForm
import org.runestar.client.api.overlay.Anchor
import org.runestar.client.api.overlay.OverlayList
import org.runestar.client.api.overlay.OverlayStack
import org.runestar.client.api.overlay.ProgressBarOverlay
import org.runestar.client.api.overlay.TextOverlay
import org.runestar.client.api.overlay.hideable
import org.runestar.client.api.overlay.withBackground
import org.runestar.client.api.overlay.withBorder
import org.runestar.client.api.overlay.withPadding
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.cacheids.StatId
import org.runestar.client.api.game.Actor
import org.runestar.client.api.game.Npc
import org.runestar.client.api.game.Player
import org.runestar.client.api.game.live.Game
import org.runestar.client.api.game.live.Players
import org.runestar.client.api.game.live.Worlds
import org.runestar.client.api.game.removeTags
import org.runestar.client.api.plugins.PluginSettings
import org.runestar.client.api.web.hiscore.HiscoreRequest
import org.runestar.client.api.web.hiscore.Hiscores
import java.awt.Color
import java.text.DecimalFormat
import kotlin.math.ceil

class OpponentInfo : DisposablePlugin<OpponentInfo.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Opponent Info"

    override fun onStart() {
        val name = TextOverlay("", settings.nameFont.value, settings.nameColor.value)
        val bar = ProgressBarOverlay(settings.hpWidth, settings.hpHeight, settings.green.value, settings.red.value)
        val num = TextOverlay("", settings.numberFont.value, settings.numberColor.value)
        val overlay = OverlayList(listOf(name, OverlayStack(num, bar)), OverlayList.DOWN, OverlayList.CENTER, settings.padding)
                .withPadding(settings.padding)
                .withBackground()
                .withBorder()
                .hideable()
        overlay.show = false

        add(settings.anchor.add(overlay))

        val percentFormatter = DecimalFormat(settings.percentFormat)

        var lastTarget: Actor? = null
        var lastHpPercent: Double? = null

        add(Game.ticks.subscribe {
            val target = Players.local?.target
            val hpPercent = target?.health
            if (hpPercent == null) {
                overlay.show = false
                return@subscribe
            }

            overlay.show = true

            if (target != lastTarget) {
                if (target is Npc) {
                    name.string = removeTags(target.type?.name.toString())
                } else if (target is Player) {
                    name.string = target.name?.name.toString()
                }
                lastTarget = target
            }

            if (hpPercent != lastHpPercent) {
                bar.progress = hpPercent
                if (target is Npc) {
                    num.string = percentFormatter.format(hpPercent)
                } else {
                    val hiscoreResult = Hiscores.lookup(HiscoreRequest.of(name.string, Worlds.hiscoreEndpoint))
                    if (hiscoreResult.isDone && !hiscoreResult.isCompletedExceptionally && hiscoreResult.get() != null) {
                        val maxHp = hiscoreResult.get()!![StatId.HITPOINTS].level
                        val curHp = ceil(hpPercent * maxHp).toInt()
                        num.string = "$curHp/$maxHp"
                    } else {
                        num.string = percentFormatter.format(hpPercent)
                    }
                }
                lastHpPercent = hpPercent
            }
        })
    }

    class Settings(
            val nameFont: FontForm = FontForm(Fonts.PLAIN_12),
            val nameColor: RgbForm = RgbForm(Color.WHITE),
            val numberFont: FontForm = FontForm(Fonts.PLAIN_12),
            val numberColor: RgbForm = RgbForm(Color.WHITE),
            val green: RgbForm = RgbForm(0, 150, 0),
            val red: RgbForm = RgbForm(150, 0, 0),
            val hpWidth: Int = 120,
            val hpHeight: Int = 18,
            val padding: Int = 1,
            val anchor: Anchor = Anchor.TOP_LEFT,
            val percentFormat: String = "0.#%"
    ) : PluginSettings()
}