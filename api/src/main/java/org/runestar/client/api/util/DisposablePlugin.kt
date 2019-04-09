package org.runestar.client.api.util

import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.internal.disposables.ListCompositeDisposable
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.Plugin
import org.runestar.client.plugins.spi.PluginSettings

/**
 * A [Plugin] that allows [Disposable] instances to be registered in the [start] method using [add] which will
 * automatically be disposed of using [Disposable.dispose] in the [stop] method.
 */
abstract class DisposablePlugin<T : PluginSettings>(
        private val listCompositeDisposable: ListCompositeDisposable
) : AbstractPlugin<T>(), DisposableContainer by listCompositeDisposable {

    constructor() : this(ListCompositeDisposable())

    final override fun start() {
        onStart()
    }

    final override fun stop() {
        listCompositeDisposable.clear()
        onStop()
    }

    protected fun add(runnable: () -> Unit) {
        add(Disposable(runnable))
    }

    protected open fun onStart() {}

    protected open fun onStop() {}

    protected fun Disposable.add() {
        add(this)
    }
}