package org.runestar.client.api.overlay

import io.reactivex.disposables.Disposable
import org.kxtra.swing.graphics.use
import org.kxtra.swing.image.createGraphics
import org.runestar.client.api.util.Disposable
import org.runestar.client.game.api.ComponentId
import org.runestar.client.game.api.live.Components
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XRasterProvider
import org.runestar.client.game.raw.access.XTextureProvider
import java.awt.Dimension
import java.awt.Graphics2D

internal object Overlays {

    private const val TOP_LEFT_OFFSET = 18

    private const val PADDING = 3

    private const val SPACING = 2

    init {
        XTextureProvider.animate.exit.subscribe {
            getGraphics().use { draw(it) }
        }
    }

    private val topLeftList = ArrayList<Overlay>()
    private val topLeft = OverlayList(topLeftList, OverlayList.DOWN, OverlayList.LEFT, SPACING)

    private val topRightList = ArrayList<Overlay>()
    private val topRight = OverlayList(topRightList, OverlayList.DOWN, OverlayList.RIGHT, SPACING)

    private val bottomLeftList = ArrayList<Overlay>()
    private val bottomLeft = OverlayList(bottomLeftList, OverlayList.RIGHT, OverlayList.DOWN, SPACING)

    private val bottomRightList = ArrayList<Overlay>()
    private val bottomRight = OverlayList(bottomRightList, OverlayList.LEFT, OverlayList.DOWN, SPACING)

    fun add(anchor: Anchor, overlay: Overlay): Disposable {
        val list = when (anchor) {
            Anchor.TOP_LEFT -> topLeftList
            Anchor.TOP_RIGHT -> topRightList
            Anchor.BOTTOM_LEFT -> bottomLeftList
            Anchor.BOTTOM_RIGHT -> bottomRightList
        }
        list.add(overlay)
        return Disposable { list.remove(overlay) }
    }

    private fun getGraphics() = (CLIENT.rasterProvider as XRasterProvider).image.createGraphics()

    private val d = Dimension()

    private fun draw(g: Graphics2D) {
        val area = Components[CLIENT.rootInterface, ComponentId.TOP_LEVEL_CONTENT_AREA]?.shape ?: return

        topLeft.getSize(g, d)
        g.translate(area.x + PADDING, area.y + PADDING + TOP_LEFT_OFFSET)
        topLeft.draw(g, d)

        bottomLeft.getSize(g, d)
        g.translate(0, area.height - d.height - PADDING * 2 - TOP_LEFT_OFFSET)
        bottomLeft.draw(g, d)
        g.translate(0, d.height - area.height + PADDING * 2)

        topRight.getSize(g, d)
        g.translate(area.width - d.width - PADDING * 2, 0)
        topRight.draw(g, d)
        g.translate(d.width, 0)

        bottomRight.getSize(g, d)
        g.translate(-d.width, area.height - d.height - PADDING * 2)
        bottomRight.draw(g, d)
    }
}