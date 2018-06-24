package org.runestar.client.plugins.smoothanimations

import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.MethodEvent
import org.runestar.client.game.raw.access.XModel
import org.runestar.client.game.raw.access.XSequenceDefinition

internal fun animateSpotAnimationEnter(event: MethodEvent<XSequenceDefinition, XModel>) {
    val frameArg = event.arguments[1] as Int
    val frame = frameArg and 0xFFFF
    event.arguments[1] = frame
    val seq = event.instance
    val nextFrame = frame + 1
    val frameCycle = (frameArg xor Int.MIN_VALUE) shr 16

    if (frameArg >= -1 || nextFrame > seq.frameIds.lastIndex || frameCycle == 0) return

    val frameId = seq.frameIds[frame]
    val frames = CLIENT.getFrames(frameId shr 16) ?: return
    val nextFrameId = seq.frameIds[nextFrame]
    val nextFrames = CLIENT.getFrames(nextFrameId shr 16) ?: return

    event.skipBody = true

    val model = event.arguments[0] as XModel
    val frameIdx = frameId and 0xFFFF
    val nextFrameIdx = nextFrameId and 0xFFFF

    val animatedModel = model.toSharedSpotAnimationModel(!frames.hasAlphaTransform(frameIdx) && !nextFrames.hasAlphaTransform(nextFrameIdx))
    animatedModel.animateInterpolated(frames.frames[frameIdx], nextFrames.frames[nextFrameIdx], frameCycle, seq.frameLengths[frame] + 1)
    event.returned = animatedModel
}