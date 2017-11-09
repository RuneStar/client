package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Projection

object Projections {

    val minimap = Projection.Minimap(LiveMinimap)

    val viewport = Projection.Viewport(LiveCamera, LiveViewport, LiveScene)
}