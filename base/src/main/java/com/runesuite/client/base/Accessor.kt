package com.runesuite.client.base

import com.jakewharton.rxrelay2.PublishRelay

interface Accessor {

    data class MethodExecution(
            val enter: PublishRelay<MethodEvent.Enter> = PublishRelay.create(),
            val exit: PublishRelay<MethodEvent.Exit> = PublishRelay.create()
    )
}