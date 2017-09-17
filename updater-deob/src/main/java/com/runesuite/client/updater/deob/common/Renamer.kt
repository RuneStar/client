package com.runesuite.client.updater.deob.common

import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import com.runesuite.client.updater.deob.writeJar
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.tree.ClassNode
import java.nio.file.Path

open class Renamer(val remapper: Remapper) : Deobfuscator {

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)
        val newClassNodes = classNodes.map {
            ClassNode(Opcodes.ASM5).apply {
                it.accept(ClassRemapper(this, remapper))
            }
        }
        writeJar(newClassNodes, destination)
    }
}