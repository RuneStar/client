package org.runestar.client.game.api

class VisibilityMap(
        val map: Array<Array<Array<BooleanArray>>>,
        val camera: Camera
) {

    fun isVisible(sceneTile: SceneTile): Boolean {
        val camTile = camera.position.sceneTile
        val vpitch = (camera.pitch.value - 128) / 32
        if (vpitch !in 0..7) return false
        val vx = sceneTile.x - camTile.x + 25
        if (vx !in 0..50) return false
        val vy = sceneTile.y - camTile.y + 25
        if (vy !in 0..50) return false
        val vyaw = camera.yaw.value / 64
        return map[vpitch][vyaw][vx][vy]
    }

    fun visibleTiles(): Sequence<SceneTile> {
        val vpitch = (camera.pitch.value - 128) / 32
        if (vpitch !in 0..7) return emptySequence()
        val vyaw = camera.yaw.value / 64
        val xymap = map[vpitch][vyaw]
        val camTile = camera.position.sceneTile
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