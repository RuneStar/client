package org.runestar.client.api.overlay

import io.reactivex.disposables.Disposable

enum class Anchor {

    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    ;

    fun add(overlay: Overlay): Disposable = Overlays.add(this, overlay)
}