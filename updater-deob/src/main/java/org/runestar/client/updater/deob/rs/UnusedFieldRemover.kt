package org.runestar.client.updater.deob.rs

import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import org.kxtra.slf4j.info
import org.kxtra.slf4j.getLogger
import org.objectweb.asm.tree.FieldInsnNode
import java.lang.reflect.Modifier
import java.nio.file.Path
import java.util.*

object UnusedFieldRemover : Transformer {

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        val usedFields = classNodes.flatMap { it.methods }
                .flatMap { it.instructions.toArray().asIterable() }
                .mapNotNull { it as? FieldInsnNode }
                .map { it.owner + "." + it.name }
                .toSet()
        val removedFields = TreeSet<String>()
        for (cn in classNodes) {
            val it = cn.fields.iterator()
            while (it.hasNext()) {
                val fn = it.next()
                val fieldName = cn.name + '.' + fn.name
                if (!usedFields.contains(fieldName) && Modifier.isFinal(fn.access)) {
                    removedFields.add(fieldName)
                    it.remove()
                }
            }
        }
        logger.info { "Fields removed: ${removedFields.size}" }
        writeJar(classNodes, destination)
    }
}