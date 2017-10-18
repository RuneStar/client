package com.runesuite.client.game.raw

import com.jakewharton.rxrelay2.PublishRelay

class MethodExecution<I, R> internal constructor() {

    @JvmField
    val enter: PublishRelay<MethodEvent.Enter<I>> = PublishRelay.create()

    @JvmField
    val exit: PublishRelay<MethodEvent.Exit<I, R>> = PublishRelay.create()
}