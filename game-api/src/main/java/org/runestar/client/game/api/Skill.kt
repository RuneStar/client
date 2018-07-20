package org.runestar.client.game.api

enum class Skill(
        val id: Int,
        val isMembersOnly: Boolean
) {
    ATTACK(0, false),
    DEFENCE(1, false),
    STRENGTH(2, false),
    HITPOINTS(3, false),
    RANGED(4, false),
    PRAYER(5, false),
    MAGIC(6, false),
    COOKING(7, false),
    WOODCUTTING(8, false),
    FLETCHING(9, true),
    FISHING(10, false),
    FIREMAKING(11, false),
    CRAFTING(12, false),
    SMITHING(13, false),
    MINING(14, false),
    HERBLORE(15, true),
    AGILITY(16, true),
    THIEVING(17, true),
    SLAYER(18, true),
    FARMING(19, true),
    RUNECRAFT(20, false),
    HUNTER(21, true),
    CONSTRUCTION(22, true);

    companion object {

        @JvmField val VALUES = values().asList()

        fun of(id: Int): Skill {
            return VALUES[id]
        }
    }
}