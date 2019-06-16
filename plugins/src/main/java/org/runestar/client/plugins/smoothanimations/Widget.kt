package org.runestar.client.plugins.smoothanimations

import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XModel
import org.runestar.client.game.raw.access.XSeqType
import org.runestar.client.game.raw.access.XComponent
import org.runestar.client.game.raw.base.MethodEvent

internal fun widgetGetModelEnter(event: MethodEvent<XComponent, XModel>) {
    val widget = event.instance
    event.arguments[1] = packFrame(widget.modelFrame, widget.modelFrameCycle)
}

internal fun animateWidgetEnter(event: MethodEvent<XSeqType, XModel>) {
    val frameArg = event.arguments[1] as Int
    val frame = frameArg and 0xFFFF
    event.arguments[1] = frame
    val seq = event.instance
    val nextFrame = frame + 1
    val frameCycle = (frameArg xor Int.MIN_VALUE) shr 16

    if (seq == null || frameArg >= -1 || nextFrame > seq.frameIds.lastIndex || frameCycle == 0) return
    val frameId1 = seq.frameIds[frame]
    val frames1 = CLIENT.getAnimFrameset(frameId1 shr 16) ?: return
    val nextFrameId1 = seq.frameIds[nextFrame]
    val nextFrames1 = CLIENT.getAnimFrameset(nextFrameId1 shr 16) ?: return

    val model = event.arguments[0] as XModel
    val frameIdx1 = frameId1 and 0xFFFF
    val nextFrameIdx1 = nextFrameId1 and 0xFFFF

    event.skipBody = true

    val frameIds2 = seq.frameIds2
    if (frameIds2 != null && frame <= frameIds2.lastIndex) {
        val frameId2 = seq.frameIds2[frame]
        val frames2 = CLIENT.getAnimFrameset(frameId1 shr 16)
        val frameIdx2 = frameId2 and 0xFFFF

        if (frames2 != null && nextFrame <= seq.frameIds2.lastIndex) {
            val nextFrameId2 = seq.frameIds2[nextFrame]
            val nextFrames2 = CLIENT.getAnimFrameset(nextFrameId2 shr 16)
            val nextFrameIdx2 = nextFrameId2 and 0xFFFF

            if (nextFrames2 != null && nextFrameIdx2 != 0xFFFF) {
                val animatedModel = model.toSharedSequenceModel(!frames1.hasAlphaTransform(frameIdx1) && !nextFrames1.hasAlphaTransform(nextFrameIdx1) &&
                        !frames2.hasAlphaTransform(frameIdx2) && !nextFrames2.hasAlphaTransform(nextFrameIdx2))
                animatedModel.animateInterpolated(frames1.frames[frameIdx1], nextFrames1.frames[nextFrameIdx1], frameCycle, seq.frameLengths[frame] + 1)
                animatedModel.animateInterpolated(frames2.frames[frameIdx2], nextFrames2.frames[nextFrameIdx2], frameCycle, seq.frameLengths[frame] + 1)
                event.returned = animatedModel
                return
            }
        }
    }

    val animatedModel = model.toSharedSequenceModel(!frames1.hasAlphaTransform(frameIdx1) && !nextFrames1.hasAlphaTransform(nextFrameIdx1))
    animatedModel.animateInterpolated(frames1.frames[frameIdx1], nextFrames1.frames[nextFrameIdx1], frameCycle, seq.frameLengths[frame] + 1)
    event.returned = animatedModel
}