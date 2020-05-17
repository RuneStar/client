package org.runestar.client.api.game.live

import org.runestar.client.api.game.LocalValue
import org.runestar.client.api.game.SceneTile
import org.runestar.client.raw.CLIENT

object VisibilityMap {

    fun isVisible(sceneTile: SceneTile): Boolean {
        val vpitch = (Camera.pitch.value - 128) / 32
        if (vpitch !in 0..7) return false
        val vx = sceneTile.x - LocalValue(Camera.localX).scene + 25
        if (vx !in 0..50) return false
        val vy = sceneTile.y - LocalValue(Camera.localY).scene + 25
        if (vy !in 0..50) return false
        val vyaw = Camera.yaw.value / 64
        return CLIENT.visibilityMap[vpitch][vyaw][vx][vy]
    }

    fun visibleTiles(): Sequence<SceneTile> {
        val vpitch = (Camera.pitch.value - 128) / 32
        if (vpitch !in 0..7) return emptySequence()
        val vyaw = Camera.yaw.value / 64
        val xymap = CLIENT.visibilityMap[vpitch][vyaw]
        val camTile = Camera.position.sceneTile
        return Sequence {
            object : AbstractIterator<SceneTile>() {

                private var x = 0

                private var y = 0

                override fun computeNext() {
                    var found = false
                    while(!found) {
                        if (xymap[x][y]) {
                            found = true
                            setNext(SceneTile(x + camTile.x - 25, y + camTile.y - 25, camTile.plane))
                        }
                        if (y == 50) {
                            if (x == 50) {
                                return done()
                            } else {
                                x++
                                y = 0
                            }
                        } else {
                            y++
                        }
                    }
                }
            }
        }
    }
}