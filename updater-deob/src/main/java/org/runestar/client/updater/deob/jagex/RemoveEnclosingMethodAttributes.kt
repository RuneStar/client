package org.runestar.client.updater.deob.jagex

import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import java.nio.file.Path

object RemoveEnclosingMethodAttributes : Transformer {

    // Byte Buddy requires classes to have accurate enclosing method attributes if they exist,
    // so we just remove them instead because they may be inaccurate from the obfuscation

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        classNodes.forEach {
            it.outerMethod = null
            it.outerMethodDesc = null
        }
        writeJar(classNodes, destination)
    }
}