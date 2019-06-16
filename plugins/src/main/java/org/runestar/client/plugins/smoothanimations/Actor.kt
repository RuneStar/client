package org.runestar.client.plugins.smoothanimations

import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XActor
import org.runestar.client.game.raw.access.XModel
import org.runestar.client.game.raw.access.XSeqType
import org.runestar.client.game.raw.base.MethodEvent

internal fun actorGetModelEnter(event: MethodEvent<out XActor, XModel>) {
    val actor = event.instance
    actor.movementFrame = packFrame(actor.movementFrame, actor.movementFrameCycle)
    actor.sequenceFrame = packFrame(actor.sequenceFrame, actor.sequenceFrameCycle)
    actor.spotAnimationFrame = packFrame(actor.spotAnimationFrame, actor.spotAnimationFrameCycle)
}

internal fun actorGetModelExit(event: MethodEvent<out XActor, XModel>) {
    val actor = event.instance
    actor.movementFrame = unpackFrame(actor.movementFrame)
    actor.sequenceFrame = unpackFrame(actor.sequenceFrame)
    actor.spotAnimationFrame = unpackFrame(actor.spotAnimationFrame)
}

internal fun animateSequenceEnter(event: MethodEvent<XSeqType, XModel>) {
    val frameArg = event.arguments[1] as Int
    val frame = frameArg and 0xFFFF
    event.arguments[1] = frame
    val seq = event.instance
    val nextFrame = frame + 1
    val frameCycle = (frameArg xor Int.MIN_VALUE) shr 16

    if (frameArg >= -1 || nextFrame > seq.frameIds.lastIndex || frameCycle == 0) return

    val frameId = seq.frameIds[frame]
    val frames = CLIENT.getAnimFrameset(frameId shr 16) ?: return
    val nextFrameId = seq.frameIds[nextFrame]
    val nextFrames = CLIENT.getAnimFrameset(nextFrameId shr 16) ?: return

    event.skipBody = true

    val model = event.arguments[0] as XModel
    val frameIdx = frameId and 0xFFFF
    val nextFrameIdx = nextFrameId and 0xFFFF

    val animatedModel = model.toSharedSequenceModel(!frames.hasAlphaTransform(frameIdx) && !nextFrames.hasAlphaTransform(nextFrameIdx))
    animatedModel.animateInterpolated(frames.frames[frameIdx], nextFrames.frames[nextFrameIdx], frameCycle, seq.frameLengths[frame] + 1)
    event.returned = animatedModel
}

internal fun animateSequence2Enter(event: MethodEvent<XSeqType, XModel>) {
    event.arguments[1] = (event.arguments[1] as Int) and 0xFFFF
    event.arguments[3] = (event.arguments[3] as Int) and 0xFFFF
}