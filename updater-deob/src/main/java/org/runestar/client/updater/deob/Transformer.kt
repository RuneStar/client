package org.runestar.client.updater.deob

import org.runestar.client.updater.deob.common.DebugRemover
import org.runestar.client.updater.deob.common.FrameRemover
import org.runestar.client.updater.deob.common.SortMethodsByLineNumber
import org.runestar.client.updater.deob.common.UnnecessaryGotoRemover
import org.runestar.client.updater.deob.common.controlflow.ControlFlowFixer
import org.runestar.client.updater.deob.jagex.JarInfo
import org.runestar.client.updater.deob.jagex.MethodOrigClassFinder
import org.runestar.client.updater.deob.jagex.MultiplierFinder
import org.runestar.client.updater.deob.jagex.OpaquePredicateFixer
import org.runestar.client.updater.deob.jagex.RemoveEnclosingMethodAttributes
import org.runestar.client.updater.deob.jagex.StaticDuplicateMethodFinder
import org.runestar.client.updater.deob.jagex.UnusedFieldRemover
import org.runestar.client.updater.deob.jagex.UnusedMethodRemover
import org.runestar.client.updater.deob.jagex.UnusedTryCatchRemover
import java.nio.file.Path

interface Transformer {

    fun transform(source: Path, destination: Path)

    class Composite(
            private val d0: Transformer,
            private vararg val ds: Transformer
    ) : Transformer {

        override fun transform(source: Path, destination: Path) {
            d0.transform(source, destination)
            ds.forEach {
                it.transform(destination, destination)
            }
        }
    }

    companion object {

        /**
         * Same resulting bytecode as [DEFAULT] but does additional analysis.
         */
        val TESTING = Transformer.Composite(
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

        val DEFAULT = Transformer.Composite(
                MultiplierFinder,
                UnusedTryCatchRemover,
                OpaquePredicateFixer,
                ControlFlowFixer,
                UnusedFieldRemover,
                UnusedMethodRemover,
                FrameRemover,
                UnnecessaryGotoRemover,
                SortMethodsByLineNumber,
                DebugRemover,
                RemoveEnclosingMethodAttributes
        )

        /**
         * Limits deobfuscation to just removing and reordering instructions. Keeps unused methods and fields. Removes
         * enclosing method attributes.
         */
        val CLEAN = Transformer.Composite(
                UnusedTryCatchRemover,
                OpaquePredicateFixer,
                ControlFlowFixer,
                FrameRemover,
                UnnecessaryGotoRemover,
                SortMethodsByLineNumber,
                DebugRemover,
                RemoveEnclosingMethodAttributes
        )
    }
}