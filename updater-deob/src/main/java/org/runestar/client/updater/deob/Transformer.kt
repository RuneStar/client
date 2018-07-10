package org.runestar.client.updater.deob

import org.runestar.client.updater.deob.common.*
import org.runestar.client.updater.deob.common.controlflow.ControlFlowFixer
import org.runestar.client.updater.deob.rs.*
import org.runestar.client.updater.deob.rs.mult.MultiplierFinder
import java.nio.file.Path

interface Transformer {

    fun transform(source: Path, destination: Path)

    class Composite(
            private val t0: Transformer,
            private vararg val ts: Transformer
    ) : Transformer {

        override fun transform(source: Path, destination: Path) {
            t0.transform(source, destination)
            ts.forEach {
                it.transform(destination, destination)
            }
        }
    }

    companion object {

        val DEFAULT = Transformer.Composite(
                FieldResolver,
                UnusedTryCatchRemover,
                OpaquePredicateCheckRemover,
                ControlFlowFixer,
                UnusedFieldRemover,
                RemoveErrorConstructors,
                UnusedMethodFinder,
                UnusedMethodRemover,
                OpaquePredicateArgumentRemover,
                FrameRemover,
                UnnecessaryGotoRemover,
                SortFieldsByModifiers,
                SortMethodsByLineNumber,
                DebugRemover,
                RemoveEnclosingMethodAttributes,
                MultiplierFinder
        )

        /**
         * Limits deobfuscation to just removing and reordering instructions. Keeps unused methods and fields. Removes
         * enclosing method attributes.
         */
        val CLEAN = Transformer.Composite(
                FieldResolver,
                OpaquePredicateCheckRemover,
                ControlFlowFixer,
                UnusedTryCatchRemover,
                RemoveErrorConstructors,
                UnusedMethodFinder,
                UnusedMethodReplacer,
                FrameRemover,
//                UnnecessaryGotoRemover,
                SortFieldsByModifiers,
                SortMethodsByLineNumber,
                DebugRemover,
                RemoveEnclosingMethodAttributes
        )
    }
}