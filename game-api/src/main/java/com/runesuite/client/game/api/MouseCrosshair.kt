package com.runesuite.client.game.api

enum class MouseCrosshair(val id: Int) {

    NONE(0),
    YELLOW(1),
    RED(2);

    companion object {

        @JvmField
        val LOOKUP = values().associateBy { it.id }
    }
}