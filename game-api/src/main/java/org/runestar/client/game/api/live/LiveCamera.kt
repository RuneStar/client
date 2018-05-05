package org.runestar.client.game.api.live

import org.runestar.client.game.api.Angle
import org.runestar.client.game.api.Camera
import org.runestar.client.game.api.Position
import org.runestar.client.game.raw.Client.accessor

object LiveCamera : Camera {

    override val localX: Int get() = accessor.cameraX.coerceIn(0, Position.MAX_LOCAL)

    override val localY: Int get() = accessor.cameraZ.coerceIn(0, Position.MAX_LOCAL)

    override val height: Int get() = LiveScene.getTileHeight(localX, localY, plane) - accessor.cameraY

    override val plane: Int get() = accessor.plane

    override val pitch get() = Angle.of(accessor.cameraPitch)

    override val yaw get() = Angle.of(accessor.cameraYaw)

    override fun toString(): String {
        return "LiveCamera(position=$position, pitch=$pitch, yaw=$yaw)"
    }
}