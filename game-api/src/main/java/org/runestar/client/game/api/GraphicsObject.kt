package org.runestar.client.game.api

import org.runestar.client.game.api.live.Scene
import org.runestar.client.game.raw.access.XGraphicsObject

class GraphicsObject(override val accessor: XGraphicsObject) : Entity(accessor) {

    override val modelPosition: Position get() = Position(accessor.x, accessor.y, heightOffset, accessor.plane)

    val heightOffset: Int get() = Scene.getTileHeight(accessor.x, accessor.y, accessor.plane) - accessor.height

    override val orientation: Angle get() = Angle.ZERO

    val id: Int get() = accessor.id
}