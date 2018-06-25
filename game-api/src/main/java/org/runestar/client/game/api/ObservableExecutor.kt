package org.runestar.client.game.api

import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.Executor

class ObservableExecutor(private val observable: Observable<*>) : Executor {

    override fun execute(command: Runnable) {
        observable.firstOrError().subscribeBy { command.run() }
    }
}