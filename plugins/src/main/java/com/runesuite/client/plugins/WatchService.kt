package com.runesuite.client.plugins

import io.reactivex.Observable
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

private fun Path.registerAll(watchService: WatchService): WatchKey {
    return register(watchService,
            StandardWatchEventKinds.ENTRY_MODIFY,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE
    )
}

internal fun newDirectoryWatchObservable(directory: Path, watchService: WatchService): Observable<WatchEvent<Path>> {
    return Observable.create<WatchEvent<Path>> { emitter ->
        Thread({
            // emit fake create events for existing files
            Files.walkFileTree(directory, object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    if (attrs.isRegularFile) {
                        emitter.onNext(object : WatchEvent<Path> {
                            override fun count() = 1
                            override fun context() = file
                            override fun kind() = StandardWatchEventKinds.ENTRY_CREATE
                        })
                    }
                    return super.visitFile(file, attrs)
                }
            })
            directory.registerAll(watchService)
            while (true) {
                val key: WatchKey
                try {
                    key = watchService.take() // blocks
                    Thread.sleep(100L) // accumulates duplicate events
                } catch (e: Exception) {
//                    emitter.onComplete()
                    return@Thread
                }
                @Suppress("UNCHECKED_CAST")
                for (event in key.pollEvents() as Collection<WatchEvent<Path>>) {
                    if (event.context() != null) {
                        emitter.onNext(event)
                    }
                }
                if (!key.reset()) {
//                    emitter.onComplete()
                    return@Thread
                }
            }
        }).start()
    }
}