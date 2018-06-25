package org.runestar.client.plugins.smoothanimations

import io.reactivex.Observable
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.access.*
import org.runestar.client.plugins.spi.PluginSettings

class SmoothAnimations : DisposablePlugin<PluginSettings>() {

    // https://github.com/runelite/runelite/commit/c66cb42a5272327adc9aeb573654911aebc75ce3
    // https://github.com/runelite/runelite/pull/3446
    // https://www.youtube.com/watch?v=BRlIuw9964c&t=44m10s

    override val name = "Smooth Animations"

    override val defaultSettings = PluginSettings()

    override fun start() {

        add(Observable.merge(XPlayer.getModel.exit, XNpc.getModel.exit).subscribe(::actorGetModelExit))
        add(Observable.merge(XPlayer.getModel.enter, XNpc.getModel.enter).subscribe(::actorGetModelEnter))
        add(XSequenceDefinition.animateSequence.enter.subscribe(::animateSequenceEnter))
        add(XSequenceDefinition.animateSequence2.enter.subscribe(::animateSequence2Enter))

        add(XWidget.getModel.enter.subscribe(::widgetGetModelEnter))
        add(XSequenceDefinition.animateWidget.enter.subscribe(::animateWidgetEnter))

        add(XSequenceDefinition.animateSpotAnimation.enter.subscribe(::animateSpotAnimationEnter))

        add(XGraphicsObject.getModel.exit.subscribe(::graphicsObjectGetModelExit))
        add(XGraphicsObject.getModel.enter.subscribe(::graphicsObjectGetModelEnter))

        add(XDynamicObject.getModel.enter.subscribe(::dynamicObjectGetModelEnter))
        add(XSequenceDefinition.animateObject.enter.subscribe(::animateObjectEnter))
    }
}

internal fun packFrame(frame: Int, frameCycle: Int): Int {
    return Int.MIN_VALUE or (frameCycle shl 16) or frame
}

internal fun unpackFrame(packed: Int): Int {
    return if (packed == -1) -1 else packed and 0xFFFF
}