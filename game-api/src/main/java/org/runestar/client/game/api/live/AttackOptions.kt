package org.runestar.client.game.api.live

import org.runestar.client.game.api.AttackOption
import org.runestar.client.game.raw.CLIENT

object AttackOptions {

    var npc: AttackOption
        get() = AttackOption.of(CLIENT.npcAttackOption)
        set(value) { CLIENT.npcAttackOption = value.accessor }

    var player: AttackOption
        get() = AttackOption.of(CLIENT.playerAttackOption)
        set(value) { CLIENT.playerAttackOption = value.accessor }
}