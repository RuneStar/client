package org.runestar.client.raw.base

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

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
    val enter: Observable<MethodEvent<I, R>>

    /**
     * [Observable] which emits an event each time the underlying method is returned from. By default all subscribers
     * [Observer.onNext] is ran synchronously before control returns to the caller. [Observer.onComplete] will never be
     * triggered.
     */
    val exit: Observable<MethodEvent<I, R>>

    /**
     * For internal use only.
     */
    class Implementation<I, R> internal constructor() : MethodExecution<I, R> {

        @JvmField
        val _enter: PublishRelay<MethodEvent<I, R>> = PublishRelay()

        override val enter: Observable<MethodEvent<I, R>> get() = _enter

        @JvmField
        val _exit: PublishRelay<MethodEvent<I, R>> = PublishRelay()

        override val exit: Observable<MethodEvent<I, R>> get() = _exit

        fun hasObservers() = _enter.hasObservers() || _exit.hasObservers()
    }
}