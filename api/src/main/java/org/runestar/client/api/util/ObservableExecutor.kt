package org.runestar.client.api.util

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.util.concurrent.Executor

class ObservableExecutor(private val observable: Observable<*>) : Executor {

    override fun execute(command: Runnable) {
        observable.firstOrError().subscribeBy { command.run() }
    }
}