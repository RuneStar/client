package org.runestar.client.plugins.smoothanimations

import io.reactivex.rxjava3.core.Observable
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.plugins.PluginSettings
import org.runestar.client.raw.access.XComponent
import org.runestar.client.raw.access.XDynamicObject
import org.runestar.client.raw.access.XGraphicsObject
import org.runestar.client.raw.access.XNpc
import org.runestar.client.raw.access.XPlayer
import org.runestar.client.raw.access.XSeqType

class SmoothAnimations : DisposablePlugin<PluginSettings>() {

    // https://github.com/runelite/runelite/commit/c66cb42a5272327adc9aeb573654911aebc75ce3
    // https://github.com/runelite/runelite/pull/3446
    // https://www.youtube.com/watch?v=BRlIuw9964c&t=44m10s

    override val name = "Smooth Animations"

    override val defaultSettings = PluginSettings()

    override fun onStart() {

        add(Observable.merge(XPlayer.getModel.exit, XNpc.getModel.exit).subscribe(::actorGetModelExit))
        add(Observable.merge(XPlayer.getModel.enter, XNpc.getModel.enter).subscribe(::actorGetModelEnter))
        add(XSeqType.animateSequence.enter.subscribe(::animateSequenceEnter))
        add(XSeqType.animateSequence2.enter.subscribe(::animateSequence2Enter))

        add(XComponent.getModel.enter.subscribe(::widgetGetModelEnter))
        add(XSeqType.animateComponent.enter.subscribe(::animateWidgetEnter))

        add(XSeqType.animateSpotAnimation.enter.subscribe(::animateSpotAnimationEnter))

        add(XGraphicsObject.getModel.exit.subscribe(::graphicsObjectGetModelExit))
        add(XGraphicsObject.getModel.enter.subscribe(::graphicsObjectGetModelEnter))

        add(XDynamicObject.getModel.enter.subscribe(::dynamicObjectGetModelEnter))
        add(XSeqType.animateObject.enter.subscribe(::animateObjectEnter))
    }
}

internal fun packFrame(frame: Int, frameCycle: Int): Int {
    return Int.MIN_VALUE or (frameCycle shl 16) or frame
}

internal fun unpackFrame(packed: Int): Int {
    return if (packed == -1) -1 else packed and 0xFFFF
}