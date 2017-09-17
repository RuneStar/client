package com.runesuite.client.updater.deob.common

import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import com.runesuite.client.updater.deob.writeJar
import org.objectweb.asm.ClassReader
import java.nio.file.Path

object FrameRemover : Deobfuscator {

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source, ClassReader.SKIP_FRAMES)
        writeJar(classNodes, destination)
    }
}