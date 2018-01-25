package org.runestar.client.utils

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.internal.disposables.ListCompositeDisposable
import org.runestar.client.plugins.Plugin
import org.runestar.client.plugins.PluginSettings

/**
 * A [Plugin] that allows [Disposable] instances to be registered in the [start] method using [add] which will
 * automatically be disposed of using [Disposable.dispose] in the [stop] method.
 */
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