package com.runesuite.client.updater.deob

import com.runesuite.client.updater.deob.common.DebugRemover
import com.runesuite.client.updater.deob.common.FrameRemover
import com.runesuite.client.updater.deob.common.SortMethodsByLineNumber
import com.runesuite.client.updater.deob.common.UnnecessaryGotoRemover
import com.runesuite.client.updater.deob.common.controlflow.ControlFlowFixer
import com.runesuite.client.updater.deob.jagex.JarInfo
import com.runesuite.client.updater.deob.jagex.MethodOrigClassFinder
import com.runesuite.client.updater.deob.jagex.MultiplierFinder
import com.runesuite.client.updater.deob.jagex.OpaquePredicateFixer
import com.runesuite.client.updater.deob.jagex.RemoveEnclosingMethodAttributes
import com.runesuite.client.updater.deob.jagex.StaticDuplicateMethodFinder
import com.runesuite.client.updater.deob.jagex.UnusedFieldRemover
import com.runesuite.client.updater.deob.jagex.UnusedMethodRemover
import com.runesuite.client.updater.deob.jagex.UnusedTryCatchRemover
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
            DebugRemover,
            RemoveEnclosingMethodAttributes
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
            DebugRemover,
            RemoveEnclosingMethodAttributes
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