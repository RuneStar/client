package com.runesuite.client.core.raw

import com.jakewharton.rxrelay2.PublishRelay

interface Accessor {

    class MethodExecution internal constructor() {
        val enter: PublishRelay<MethodEvent.Enter> = PublishRelay.create()
        val exit: PublishRelay<MethodEvent.Exit> = PublishRelay.create()
    }
}