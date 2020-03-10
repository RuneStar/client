package org.runestar.client.updater.deob.rs

import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.runestar.client.updater.deob.Transformer
import java.lang.reflect.Modifier
import java.nio.file.Path
import java.util.TreeSet

object UnusedFieldRemover : Transformer.Tree() {

    private val logger = getLogger()

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        val usedFields = klasses.flatMap { it.methods }
                .flatMap { it.instructions.toArray().asIterable() }
                .mapNotNull { it as? FieldInsnNode }
                .map { it.owner + "." + it.name }
                .toSet()
        val removedFields = TreeSet<String>()
        for (cn in klasses) {
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
    }
}