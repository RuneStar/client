package org.runestar.client.api.game.live

import org.runestar.client.api.game.GraphicsObject
import org.runestar.client.api.game.NodeDeque
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XGraphicsObject

object GraphicsObjects : NodeDeque<GraphicsObject, XGraphicsObject>(CLIENT.graphicsObjects) {

    override fun wrap(n: XGraphicsObject): GraphicsObject = GraphicsObject(n)
}