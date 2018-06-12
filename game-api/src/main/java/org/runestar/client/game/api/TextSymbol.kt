package org.runestar.client.game.api

interface TextSymbol {

    val tag: String

    enum class Simple(override val tag: String) : TextSymbol {
        BREAK("<br>"),
        GT("<gt>"),
        LT("<lt>")
    }

    data class Image(val index: Int) : TextSymbol {

        override val tag: String = "<img=$index>"
    }
}