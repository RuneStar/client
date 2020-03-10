package org.runestar.client.updater.deob.common

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LineNumberNode
import org.objectweb.asm.tree.MethodNode
import org.runestar.client.updater.deob.Transformer
import java.nio.file.Path

object SortMethodsByLineNumber : Transformer.Tree() {

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        klasses.forEach { k ->
            val lineNums = k.methods.associateWith { (it.firstLineNum() ?: Integer.MAX_VALUE) }
            k.methods = k.methods.sortedBy { lineNums.getValue(it) }
        }
    }

    private fun MethodNode.firstLineNum(): Int? {
        for (insn in instructions) {
            if (insn is LineNumberNode) {
                return insn.line
            }
        }
        return null
    }
}
