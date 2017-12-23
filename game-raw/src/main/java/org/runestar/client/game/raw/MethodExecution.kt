package org.runestar.client.game.raw

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Observer
import java.util.concurrent.atomic.AtomicLong

/**
 * Wraps a method and provides access to the state of the method calls both before execution starts and after it ends.
 *
 * @param[I] the instance type this method is declared on, [Void] for `static` methods
 * @param[R] the return type of this method, wrappers for primitives, [Void] for `void` methods
 */
interface MethodExecution<I, R> {

    /**
     * [Observable] which emits an event each time the underlying method is called. By default all subscribers
     * [Observer.onNext] is ran synchronously before the body of the method is executed. [Observer.onComplete] will
     * never be triggered.
     */
    val enter: Observable<MethodEvent.Enter<I>>

    /**
     * [Observable] which emits an event each time the underlying method is returned from. By default all subscribers
     * [Observer.onNext] is ran synchronously before control returns to the caller. [Observer.onComplete] will never be
     * triggered.
     */
    val exit: Observable<MethodEvent.Exit<I, R>>

    /**
     * For internal use only.
     */
    class Implementation<I, R> : MethodExecution<I, R> {

        @JvmField
        val counter = AtomicLong(0)

        override val enter = PublishRelay.create<MethodEvent.Enter<I>>()

        override val exit = PublishRelay.create<MethodEvent.Exit<I, R>>()
    }
}