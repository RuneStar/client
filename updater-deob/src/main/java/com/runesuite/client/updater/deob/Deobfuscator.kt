package com.runesuite.client.updater.deob

import com.runesuite.client.updater.deob.common.DebugRemover
import com.runesuite.client.updater.deob.common.FrameRemover
import com.runesuite.client.updater.deob.common.SortMethodsByLineNumber
import com.runesuite.client.updater.deob.common.UnnecessaryGotoRemover
import com.runesuite.client.updater.deob.common.controlflow.ControlFlowFixer
import com.runesuite.client.updater.deob.jagex.*
import java.nio.file.Path

interface Deobfuscator {

    object All : Deobfuscator.Composite(
            JarInfo,
            MultiplierFinder,
            UnusedTryCatchRemover,
            OpaquePredicateFixer,
            ControlFlowFixer,
            StaticDuplicateMethodFinder,
            UnusedFieldRemover,
            UnusedMethodRemover,
            FrameRemover,
            UnnecessaryGotoRemover,
            SortMethodsByLineNumber,
            MethodOrigClassFinder,
            DebugRemover
    )

    object Faster : Deobfuscator.Composite(
//            JarInfo,
            MultiplierFinder,
            UnusedTryCatchRemover,
            OpaquePredicateFixer,
            ControlFlowFixer,
//            StaticDuplicateMethodFinder,
            UnusedFieldRemover,
            UnusedMethodRemover,
            FrameRemover,
            UnnecessaryGotoRemover,
//            SortMethodsByLineNumber,
//            MethodOrigClassFinder,
            DebugRemover
    )

    fun deob(source: Path, destination: Path)

    open class Composite(private vararg val deobfuscators: Deobfuscator) : Deobfuscator {

        init {
            require(deobfuscators.isNotEmpty())
        }

        override fun deob(source: Path, destination: Path) {
            deobfuscators[0].deob(source, destination)
            for (i in 1 until deobfuscators.size) {
                deobfuscators[i].deob(destination, destination)
            }
        }
    }
}