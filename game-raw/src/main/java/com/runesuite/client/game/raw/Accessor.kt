package com.runesuite.client.game.raw

import com.jakewharton.rxrelay2.PublishRelay

interface Accessor {

    class MethodExecution internal constructor() {
        @JvmField
        val enter: PublishRelay<MethodEvent.Enter> = PublishRelay.create()
        @JvmField
        val exit: PublishRelay<MethodEvent.Exit> = PublishRelay.create()
    }
}