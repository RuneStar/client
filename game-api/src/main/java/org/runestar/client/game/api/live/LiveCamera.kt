package org.runestar.client.game.api.live

import org.runestar.client.game.api.Angle
import org.runestar.client.game.api.Camera
import org.runestar.client.game.api.Position
import org.runestar.client.game.raw.CLIENT

object LiveCamera : Camera {

    override val localX: Int get() = CLIENT.cameraX.coerceIn(0, Position.MAX_LOCAL)

    override val localY: Int get() = CLIENT.cameraZ.coerceIn(0, Position.MAX_LOCAL)

    override val absoluteHeight: Int get() = CLIENT.cameraY

    override val height: Int get() = LiveScene.getTileHeight(localX, localY, plane) - absoluteHeight

    override val plane: Int get() = CLIENT.plane

    override val pitch get() = Angle.of(CLIENT.cameraPitch)

    override val yaw get() = Angle.of(CLIENT.cameraYaw)

    override fun toString(): String {
        return "LiveCamera(position=$position, pitch=$pitch, yaw=$yaw)"
    }
}