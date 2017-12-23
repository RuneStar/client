package org.runestar.client.game.api.live

import org.runestar.client.game.api.Projection

object Projections {

    val minimap = Projection.Minimap(LiveMinimap)

    val viewport = Projection.Viewport(LiveCamera, LiveViewport, LiveScene)
}