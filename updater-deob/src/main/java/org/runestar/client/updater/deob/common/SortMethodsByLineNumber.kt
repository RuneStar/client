package org.runestar.client.updater.deob.common

import org.objectweb.asm.tree.LineNumberNode
import org.objectweb.asm.tree.MethodNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import java.nio.file.Path

object SortMethodsByLineNumber : Transformer {

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        classNodes.forEach { c ->
            val lineNums = c.methods.associate { it to (it.firstLineNum() ?: Integer.MAX_VALUE) }
            c.methods = c.methods.sortedBy { lineNums.getValue(it) }
        }
        writeJar(classNodes, destination)
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
