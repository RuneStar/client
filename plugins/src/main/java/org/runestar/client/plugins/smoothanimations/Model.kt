package org.runestar.client.plugins.smoothanimations

import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XAnimation
import org.runestar.client.game.raw.access.XModel

internal fun XModel.animateInterpolated(
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
                transformX = lerpMod256(currentTransformX and 0xFF, nextTransformX and 0xFF, frameCycle, frameLength)
                transformY = lerpMod256(currentTransformY and 0xFF, nextTransformY and 0xFF, frameCycle, frameLength)
                transformZ = lerpMod256(currentTransformZ and 0xFF, nextTransformZ and 0xFF, frameCycle, frameLength)
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

private fun lerpMod256(v0: Int, v1: Int, tn: Int, td: Int): Int {
    return (v0 + (v1 - v0).toByte() * tn / td) and 0xFF
}

private fun lerp(v0: Int, v1: Int, tn: Int, td: Int): Int {
    return v0 + (v1 - v0) * tn / td
}