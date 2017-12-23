package org.runestar.client.updater.deob.common

import org.runestar.client.updater.deob.Deobfuscator
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import org.objectweb.asm.ClassReader
import java.nio.file.Path

object DebugRemover : Deobfuscator {

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source, ClassReader.SKIP_DEBUG)
        writeJar(classNodes, destination)
    }
}