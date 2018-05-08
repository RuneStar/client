package org.runestar.client.updater.deob

import org.runestar.client.updater.deob.common.*
import org.runestar.client.updater.deob.common.controlflow.ControlFlowFixer
import org.runestar.client.updater.deob.jagex.*
import org.runestar.client.updater.deob.jagex.mult.MultiplierFinder
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
                FieldResolver,
                MultiplierFinder,
                UnusedTryCatchRemover,
                OpaquePredicateCheckRemover,
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
//                OpaquePredicateArgumentRemover
        )

        val DEFAULT = Transformer.Composite(
                FieldResolver,
                MultiplierFinder,
                UnusedTryCatchRemover,
                OpaquePredicateCheckRemover,
                ControlFlowFixer,
                UnusedFieldRemover,
                UnusedMethodRemover,
                FrameRemover,
                UnnecessaryGotoRemover,
                SortMethodsByLineNumber,
                DebugRemover,
                RemoveEnclosingMethodAttributes
//                OpaquePredicateArgumentRemover
        )

        /**
         * Limits deobfuscation to just removing and reordering instructions. Keeps unused methods and fields. Removes
         * enclosing method attributes.
         */
        val CLEAN = Transformer.Composite(
                FieldResolver,
                UnusedTryCatchRemover,
                OpaquePredicateCheckRemover,
                ControlFlowFixer,
                FrameRemover,
//                UnnecessaryGotoRemover,
                SortMethodsByLineNumber,
                DebugRemover,
                RemoveEnclosingMethodAttributes
        )
    }
}