package org.runestar.client.plugins.smoothanimations

import org.runestar.client.game.raw.MethodEvent
import org.runestar.client.game.raw.access.XGraphicsObject
import org.runestar.client.game.raw.access.XModel

internal fun graphicsObjectGetModelEnter(event: MethodEvent<XGraphicsObject, XModel>) {
    val obj = event.instance
    obj.frame = packFrame(obj.frame, obj.frameCycle)
}

internal fun graphicsObjectGetModelExit(event: MethodEvent<XGraphicsObject, XModel>) {
    val obj = event.instance
    obj.frame = unpackFrame(obj.frame)
}