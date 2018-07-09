package org.runestar.client.updater.deob.common

import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.tree.ClassNode
import java.nio.file.Path

class Renamer(val remapper: Remapper) : Transformer {

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        val newClassNodes = classNodes.map {
            ClassNode(Opcodes.ASM5).apply {
                it.accept(ClassRemapper(this, remapper))
            }
        }
        writeJar(newClassNodes, destination)
    }
}