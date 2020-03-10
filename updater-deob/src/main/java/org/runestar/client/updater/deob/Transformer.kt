package org.runestar.client.updater.deob

import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import org.runestar.client.updater.deob.common.*
import org.runestar.client.updater.deob.common.controlflow.ControlFlowFixer
import org.runestar.client.updater.deob.rs.*
import org.runestar.client.updater.deob.util.ClassNode
import org.runestar.client.updater.deob.util.toByteArray
import java.nio.file.Path

interface Transformer {

    fun transform(dir: Path, klasses: List<ByteArray>): List<ByteArray>

    abstract class Tree : Transformer {

        final override fun transform(dir: Path, klasses: List<ByteArray>): List<ByteArray> {
            val nodes = klasses.map { ClassNode(it) }
            transform(dir, nodes)
            return nodes.map { it.toByteArray() }
        }

        abstract fun transform(dir: Path, klasses: List<ClassNode>)
    }

    class Composite(vararg val transformers: Transformer) : Transformer {

        override fun transform(dir: Path, klasses: List<ByteArray>) = transformers.fold(klasses) { acc, t -> t.transform(dir, acc) }
    }

    companion object {

        val DEFAULT = Composite(
                Parser(ClassReader.SKIP_FRAMES, 0),
                FieldResolver,
                UnusedTryCatchRemover,
                ControlFlowFixer,
                OpaquePredicateCheckRemover,
                UnusedFieldRemover,
                RemoveErrorConstructors,
                UnusedMethodFinder,
                UnusedMethodRemover,
                OpaquePredicateArgumentRemover,
                UnnecessaryGotoRemover,
                SortFieldsByModifiers,
                SortMethodsByLineNumber,
                Parser(ClassReader.SKIP_DEBUG, 0),
                RemoveEnclosingMethodAttributes,
                MultiplierFinder,
                MultiplierFixer
        )

        /**
         * Limits deobfuscation to just removing and reordering instructions. Keeps unused methods and fields. Removes
         * enclosing method attributes.
         */
        val CLEAN = Composite(
                Parser(ClassReader.SKIP_FRAMES, 0),
                FieldResolver,
                UnusedTryCatchRemover,
                ControlFlowFixer,
                OpaquePredicateCheckRemover,
                RemoveErrorConstructors,
                UnusedMethodFinder,
                UnusedMethodReplacer,
                UnnecessaryGotoRemover,
                SortFieldsByModifiers,
                SortMethodsByLineNumber,
                Parser(ClassReader.SKIP_DEBUG, 0),
                RemoveEnclosingMethodAttributes,
                ComputeFrames
        )
    }
}