package com.runesuite.client.updater.deob.common

import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import com.runesuite.client.updater.deob.writeJar
import org.objectweb.asm.tree.LineNumberNode
import java.nio.file.Path

object SortMethodsByLineNumber : Deobfuscator {

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)
        classNodes.forEach { c ->
            c.methods =  c.methods.sortedBy {
                it.instructions.iterator().asSequence().mapNotNull { it as? LineNumberNode }.firstOrNull()?.line ?: Integer.MAX_VALUE }
        }
        writeJar(classNodes, destination)
    }
}
