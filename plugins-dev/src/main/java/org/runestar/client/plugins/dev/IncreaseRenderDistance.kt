package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XScene
import org.runestar.client.api.plugins.PluginSettings

class IncreaseRenderDistance : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(XScene.draw.enter.subscribe {
            it.skipBody = true
            val scene = it.instance

            var camX = it.arguments[0] as Int
            val camY = it.arguments[1] as Int
            var camZ = it.arguments[2] as Int
            var pitch = it.arguments[3] as Int
            val yaw = it.arguments[4] as Int
            val plane = it.arguments[5] as Int

            camX = camX.coerceIn(0, scene.xSize * 128 - 1)
            camZ = camZ.coerceIn(0, scene.ySize * 128 - 1)
            pitch = pitch.coerceIn(128, 383)

            CLIENT.scene_cameraPitchSine = CLIENT.rasterizer3D_sine[pitch];
            CLIENT.scene_cameraPitchCosine = CLIENT.rasterizer3D_cosine[pitch];
            CLIENT.scene_cameraYawSine = CLIENT.rasterizer3D_sine[yaw];
            CLIENT.scene_cameraYawCosine = CLIENT.rasterizer3D_cosine[yaw];
            CLIENT.scene_cameraX = camX;
            CLIENT.scene_cameraY = camY;
            CLIENT.scene_cameraZ = camZ;
            CLIENT.scene_cameraXTile = camX / 128;
            CLIENT.scene_cameraYTile = camZ / 128;
            CLIENT.scene_plane = plane;

            CLIENT.scene_cameraXTileMin = 0
            CLIENT.scene_cameraYTileMin = 0
            CLIENT.scene_cameraXTileMax = scene.xSize
            CLIENT.scene_cameraYTileMax = scene.ySize

            CLIENT.scene_drawnCount++
            CLIENT.scene_currentOccludersCount = 0

            for (p in scene.minPlane..3) {
                for (x in 0..103) {
                    for (y in 0..103) {
                        val tile = scene.tiles[p][x][y] ?: continue

                        val dx = x * 128 - camX
                        val dy = CLIENT.tiles_heights[p][x][y] - camY
                        val dz = y * 128 - camZ

                        val var11 = (dz * CLIENT.scene_cameraYawCosine - dx * CLIENT.scene_cameraYawSine) shr 16
                        val var12 = (dy * CLIENT.scene_cameraPitchSine + var11 * CLIENT.scene_cameraPitchCosine) shr 16

                        if (tile.minPlane > plane || var12 >= 3500) {
                            tile.drawPrimary = false
                            tile.drawSecondary = false
                            tile.drawSceneryEdges = 0
                        } else {
                            tile.drawPrimary = true
                            tile.drawSecondary = true
                            tile.drawScenery = tile.sceneryCount > 0
                        }
                    }
                }
            }

            for (p in scene.minPlane..3) {
                for (x in 0..103) {
                    for (y in 0..103) {
                        val tile = scene.tiles[p][x][y] ?: continue
                        if (tile.drawPrimary) {
                            scene.drawTile(tile, true)
                        }
                    }
                }
            }

            for (p in scene.minPlane..3) {
                for (x in 0..103) {
                    for (y in 0..103) {
                        val tile = scene.tiles[p][x][y] ?: continue
                        if (tile.drawPrimary) {
                            scene.drawTile(tile, false)
                        }
                    }
                }
            }
        })
    }
}