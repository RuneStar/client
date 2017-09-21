package com.runesuite.client.updater.deob.jagex

import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import com.runesuite.client.updater.deob.writeJar
import java.nio.file.Path

object RemoveEnclosingMethodAttributes : Deobfuscator {

    // Byte Buddy requires classes to have accurate enclosing method attributes if they exist,
    // so we just remove them instead because they may be inaccurate from the obfuscation

    override fun deob(source: Path, destination: Path) {
        val classNodes = readJar(source)
        classNodes.forEach {
            it.outerMethod = null
            it.outerMethodDesc = null
        }
        writeJar(classNodes, destination)
    }
}