package com.runesuite.client.plugins.utils

import com.runesuite.client.plugins.Plugin
import com.runesuite.client.plugins.PluginSettings
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.internal.disposables.ListCompositeDisposable
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class DisposablePlugin<T : PluginSettings>(
        private val listCompositeDisposable: ListCompositeDisposable
) : Plugin<T>(), DisposableContainer by listCompositeDisposable {

    constructor() : this(ListCompositeDisposable())

    @OverridingMethodsMustInvokeSuper
    override fun stop() {
        super.stop()
        listCompositeDisposable.clear()
    }
}