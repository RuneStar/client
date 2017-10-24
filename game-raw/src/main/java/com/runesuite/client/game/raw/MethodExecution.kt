package com.runesuite.client.game.raw

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import java.util.concurrent.atomic.AtomicLong

interface MethodExecution<I, R> {

    /**
     * Number of times this method has been called.
     */
    val count: Long

    val enter: Observable<MethodEvent.Enter<I>>

    val exit: Observable<MethodEvent.Exit<I, R>>

    /**
     * For internal use only.
     */
    class Implementation<I, R> : MethodExecution<I, R> {

        @JvmField
        val counter = AtomicLong(0)

        override val count get() = counter.get()

        override val enter = PublishRelay.create<MethodEvent.Enter<I>>()

        override val exit = PublishRelay.create<MethodEvent.Exit<I, R>>()
    }
}