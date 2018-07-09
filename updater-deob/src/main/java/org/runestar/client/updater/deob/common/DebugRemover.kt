package org.runestar.client.updater.deob.common

import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import org.objectweb.asm.ClassReader
import java.nio.file.Path

object DebugRemover : Transformer {

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source, ClassReader.SKIP_DEBUG)
        writeJar(classNodes, destination)
    }
}