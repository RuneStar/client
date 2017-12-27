package org.runestar.client.utils

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.internal.disposables.ListCompositeDisposable
import org.runestar.client.plugins.Plugin
import org.runestar.client.plugins.PluginSettings

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