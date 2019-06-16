package org.runestar.client.plugins.smoothanimations

import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XDynamicObject
import org.runestar.client.game.raw.access.XModel
import org.runestar.client.game.raw.access.XSeqType
import org.runestar.client.game.raw.base.MethodEvent

private var frameCycle = -1

internal fun dynamicObjectGetModelEnter(event: MethodEvent<XDynamicObject, XModel>) {
    val obj = event.instance
    val seq = obj.seqType ?: return
    var cycle = CLIENT.cycle - obj.cycleStart
    var frame = obj.frame

    if (cycle > 100 && seq.frameCount > 0) {
        cycle = 100
    }

    label@ do {
        do {
            if (cycle <= seq.frameLengths[frame]) {
                break@label
            }
            cycle -= seq.frameLengths[frame]
            frame++
        } while (frame < seq.frameIds.size)
        frame -= seq.frameCount
    } while (frame >= 0 && frame < seq.frameIds.size)

    frameCycle = cycle
}

internal fun animateObjectEnter(event: MethodEvent<XSeqType, XModel>) {
    val frame = event.arguments[1] as Int
    val rotation = event.arguments[2] as Int
    val seq = event.instance
    val nextFrame = frame + 1

    if (frame == -1 || nextFrame > seq.frameIds.lastIndex || frameCycle <= 0) return

    val frameId = seq.frameIds[frame]
    val frames = CLIENT.getAnimFrameset(frameId shr 16) ?: return
    val nextFrameId = seq.frameIds[nextFrame]
    val nextFrames = CLIENT.getAnimFrameset(nextFrameId shr 16) ?: return

    event.skipBody = true

    val model = event.arguments[0] as XModel
    val frameIdx = frameId and 0xFFFF
    val nextFrameIdx = nextFrameId and 0xFFFF

    val animatedModel = model.toSharedSequenceModel(!frames.hasAlphaTransform(frameIdx) && !nextFrames.hasAlphaTransform(nextFrameIdx))
    when (rotation) {
        1 -> animatedModel.rotateY270Ccw()
        2 -> animatedModel.rotateY180()
        3 -> animatedModel.rotateY90Ccw()
    }
    animatedModel.animateInterpolated(frames.frames[frameIdx], nextFrames.frames[nextFrameIdx], frameCycle, seq.frameLengths[frame] + 1)
    when (rotation) {
        1 -> animatedModel.rotateY90Ccw()
        2 -> animatedModel.rotateY180()
        3 -> animatedModel.rotateY270Ccw()
    }
    event.returned = animatedModel
}