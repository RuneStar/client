package org.runestar.client.game.api

inline class LocalValue(val value: Int) {

    constructor(scene: Int, sub: Int) : this((scene shl 7) or sub)

    val scene: Int get() = value ushr 7

    val sub: Int get() = value and MAX_SUB

    operator fun plus(other: LocalValue): LocalValue = LocalValue(value + other.value)

    override fun toString() = "LocalValue(scene=$scene, sub=$sub)"

    companion object {
        const val MAX_SUB = 0x7F
        const val MID_SUB = 64
        val MAX = LocalValue(Scene.SIZE - 1, MAX_SUB)
    }
}