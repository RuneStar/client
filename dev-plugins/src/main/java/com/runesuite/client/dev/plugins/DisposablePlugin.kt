package com.runesuite.client.dev.plugins

import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.internal.disposables.ListCompositeDisposable
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class DisposablePlugin<T : Plugin.Settings>(
        private val disposeOnStopList: ListCompositeDisposable
) : Plugin<T>(), DisposableContainer by disposeOnStopList {

    constructor() : this(ListCompositeDisposable())

    @OverridingMethodsMustInvokeSuper
    override fun stop() {
        super.stop()
        disposeOnStopList.clear()
    }
}