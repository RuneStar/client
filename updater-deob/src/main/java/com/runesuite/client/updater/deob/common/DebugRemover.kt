package com.runesuite.client.updater.deob.common

import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import com.runesuite.client.updater.deob.writeJar
import org.objectweb.asm.ClassReader
import java.nio.file.Path

object DebugRemover : Deobfuscator {

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source, ClassReader.SKIP_DEBUG)
        writeJar(classNodes, destination)
    }
}