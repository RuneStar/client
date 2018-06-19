package org.runestar.client.game.api.live

import org.runestar.client.game.api.GraphicsObject
import org.runestar.client.game.api.NodeDeque
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XGraphicsObject
import org.runestar.client.game.raw.access.XNode

object GraphicsObjects : NodeDeque<GraphicsObject>(CLIENT.graphicsObjects) {

    override fun wrap(t: XNode): GraphicsObject {
        return GraphicsObject(t as XGraphicsObject)
    }
}