package org.runestar.client.plugins.smoothanimations

import io.reactivex.Observable
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.MethodEvent
import org.runestar.client.game.raw.access.*
import org.runestar.client.plugins.spi.PluginSettings

class SmoothAnimations : DisposablePlugin<PluginSettings>() {

    // https://github.com/runelite/runelite/commit/c66cb42a5272327adc9aeb573654911aebc75ce3
    // https://github.com/runelite/runelite/pull/3446
    // https://www.youtube.com/watch?v=BRlIuw9964c&t=44m10s

    override val name = "Smooth Animations"

    override val defaultSettings = PluginSettings()

    override fun start() {

        add(Observable.merge(XPlayer.getModel.enter, XNpc.getModel.enter).subscribe {
            val actor = it.instance
            actor.movementFrame = Int.MIN_VALUE or (actor.movementFrameCycle shl 16) or actor.movementFrame
            actor.sequenceFrame = Int.MIN_VALUE or (actor.sequenceFrameCycle shl 16) or actor.sequenceFrame
            actor.spotAnimationFrame = Int.MIN_VALUE or (actor.spotAnimationFrameCycle shl 16) or actor.spotAnimationFrame
        })

        add(Observable.merge(XPlayer.getModel.exit, XNpc.getModel.exit).subscribe {
            val actor = it.instance
            if (actor.movementFrame != -1) actor.movementFrame = actor.movementFrame and 0xFFFF
            if (actor.sequenceFrame != -1) actor.sequenceFrame = actor.sequenceFrame and 0xFFFF
            if (actor.spotAnimationFrame != -1) actor.spotAnimationFrame = actor.spotAnimationFrame and 0xFFFF
        })

        add(XSequenceDefinition.animateSequence.enter.subscribe { animateSequenceEnter(it, false) })

        add(XSequenceDefinition.animateSpotAnimation.enter.subscribe { animateSequenceEnter(it, true) })

        add(XSequenceDefinition.animateSequence2.enter.subscribe {
            val frame = it.arguments[1] as Int
            if (frame < 0) {
                it.arguments[1] = frame and 0xFFFF
            }
            val sequenceFrame = it.arguments[3] as Int
            if (sequenceFrame < 0) {
                it.arguments[3] = sequenceFrame and 0xFFFF
            }
        })
    }

    private fun animateSequenceEnter(
            event: MethodEvent<XSequenceDefinition, XModel>,
            isSpotAnimation: Boolean
    ) {
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

        val animatedModel = if (isSpotAnimation) {
            model.toSharedSpotAnimationModel(!frames.hasAlphaTransform(frameIdx))
        } else {
            model.toSharedSequenceModel(!frames.hasAlphaTransform(frameIdx))
        }
        animatedModel.animateInterpolated(frames.frames[frameIdx], nextFrames.frames[nextFrameIdx], frameCycle, seq.frameLengths[frame] + 1)
        event.returned = animatedModel
    }

    private fun XModel.animateInterpolated(
            animation: XAnimation,
            nextAnimation: XAnimation,
            frameCycle: Int,
            frameLength: Int
    ) {
        if (vertexLabels == null) return
        val skeleton = animation.skeleton
        check(skeleton == nextAnimation.skeleton)

        CLIENT.model_transformTempX = 0
        CLIENT.model_transformTempY = 0
        CLIENT.model_transformTempZ = 0

        var transformIndex = 0
        var nextTransformIndex = 0

        repeat(skeleton.count) { i ->

            val frameValid = transformIndex < animation.transformCount && animation.transformSkeletonLabels[transformIndex] == i
            val nextFrameValid = nextTransformIndex < nextAnimation.transformCount && nextAnimation.transformSkeletonLabels[nextTransformIndex] == i

            if (!frameValid && !nextFrameValid) return@repeat

            val type = skeleton.transformTypes[i]

            val defaultTransformValue = if (type == 3) 128 else 0

            val currentTransformX: Int
            val currentTransformY: Int
            val currentTransformZ: Int
            if (frameValid) {
                currentTransformX = animation.transformXs[transformIndex]
                currentTransformY = animation.transformYs[transformIndex]
                currentTransformZ = animation.transformZs[transformIndex]
                transformIndex++
            } else {
                currentTransformX = defaultTransformValue
                currentTransformY = defaultTransformValue
                currentTransformZ = defaultTransformValue
            }

            val nextTransformX: Int
            val nextTransformY: Int
            val nextTransformZ: Int
            if (nextFrameValid) {
                nextTransformX = nextAnimation.transformXs[nextTransformIndex]
                nextTransformY = nextAnimation.transformYs[nextTransformIndex]
                nextTransformZ = nextAnimation.transformZs[nextTransformIndex]
                nextTransformIndex++
            } else {
                nextTransformX = defaultTransformValue
                nextTransformY = defaultTransformValue
                nextTransformZ = defaultTransformValue
            }

            val transformX: Int
            val transformY: Int
            val transformZ: Int
            when (type) {
                2 -> {
                    transformX = lerpAngle256(currentTransformX and 0xFF, nextTransformX and 0xFF, frameCycle, frameLength)
                    transformY = lerpAngle256(currentTransformY and 0xFF, nextTransformY and 0xFF, frameCycle, frameLength)
                    transformZ = lerpAngle256(currentTransformZ and 0xFF, nextTransformZ and 0xFF, frameCycle, frameLength)
                }
                5 -> {
                    transformX = currentTransformX
                    transformY = 0
                    transformZ = 0
                }
                else -> { // 0, 1, 3
                    transformX = lerp(currentTransformX, nextTransformX, frameCycle, frameLength)
                    transformY = lerp(currentTransformY, nextTransformY, frameCycle, frameLength)
                    transformZ = lerp(currentTransformZ, nextTransformZ, frameCycle, frameLength)
                }
            }
            transform(type, skeleton.labels[i], transformX, transformY, transformZ)
        }
        resetBounds()
    }

    private fun lerpAngle256(v0: Int, v1: Int, tn: Int, td: Int): Int {
        return (v0 + (v1 - v0).toByte() * tn / td) and 0xFF
    }

    private fun lerp(v0: Int, v1: Int, tn: Int, td: Int): Int {
        return v0 + (v1 - v0) * tn / td
    }
}