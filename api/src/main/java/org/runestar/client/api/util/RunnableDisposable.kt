package org.runestar.client.api.util

import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean

fun Disposable(runnable: () -> Unit): Disposable = RunnableDisposable(runnable)

private class RunnableDisposable(
        private val runnable: () -> Unit
) : Disposable, AtomicBoolean() {

    override fun dispose() {
        if (compareAndSet(false, true)) {
            runnable()
        }
    }

    override fun isDisposed() = get()
}