package org.runestar.client.updater.deob.common

import org.objectweb.asm.tree.LineNumberNode
import org.objectweb.asm.tree.MethodNode
import org.runestar.client.updater.deob.Deobfuscator
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import java.nio.file.Path

object SortMethodsByLineNumber : Deobfuscator {

    override fun deob(source: Path, destination: Path) {
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
