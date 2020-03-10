package org.runestar.client.updater.deob.rs

import org.objectweb.asm.tree.ClassNode
import org.runestar.client.updater.deob.Transformer
import java.nio.file.Path

object RemoveEnclosingMethodAttributes : Transformer.Tree() {

    // Byte Buddy requires classes to have accurate enclosing method attributes if they exist,
    // so we just remove them instead because they may be inaccurate from the obfuscation

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        klasses.forEach { k ->
            k.outerMethod = null
            k.outerMethodDesc = null
        }
    }
}