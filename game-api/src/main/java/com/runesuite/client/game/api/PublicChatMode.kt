package com.runesuite.client.game.api

enum class PublicChatMode(val id: Int) {

    STANDARD(0),
    FRIENDS(1),
    OFF(2),
    HIDE(3),
    AUTOCHAT(4);

    companion object {

        @JvmField
        val LOOKUP = values().associateBy { it.id }
    }
}