package com.runesuite.client.game.raw

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import java.util.concurrent.atomic.AtomicLong

class MethodExecution<I, R> internal constructor() {

    /**
     * For internal use only
     */
    @JvmField
    val _count = AtomicLong(0)

    /**
     * For internal use only
     */
    @JvmField
    val _enter: PublishRelay<MethodEvent.Enter<I>> = PublishRelay.create()

    /**
     * For internal use only
     */
    @JvmField
    val _exit: PublishRelay<MethodEvent.Exit<I, R>> = PublishRelay.create()

    /**
     * Number of times this method has been called.
     */
    val count get() = _count.get()

    val enter get(): Observable<MethodEvent.Enter<I>> = _enter

    val exit get(): Observable<MethodEvent.Exit<I, R>> = _exit
}