package org.runestar.client.plugins.smoothanimations

import org.runestar.client.game.raw.access.XGraphicsObject
import org.runestar.client.game.raw.access.XModel
import org.runestar.client.game.raw.base.MethodEvent

internal fun graphicsObjectGetModelEnter(event: MethodEvent<XGraphicsObject, XModel>) {
    val obj = event.instance
    obj.frame = packFrame(obj.frame, obj.frameCycle)
}

internal fun graphicsObjectGetModelExit(event: MethodEvent<XGraphicsObject, XModel>) {
    val obj = event.instance
    obj.frame = unpackFrame(obj.frame)
}