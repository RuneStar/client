package com.runesuite.client.updater.deob.jagex

import com.runesuite.client.updater.deob.Deobfuscator
import com.runesuite.client.updater.deob.readJar
import com.runesuite.client.updater.deob.writeJar
import mu.KotlinLogging
import org.objectweb.asm.tree.FieldInsnNode
import java.lang.reflect.Modifier
import java.nio.file.Path
import java.util.*

object UnusedFieldRemover : Deobfuscator {

    private val logger = KotlinLogging.logger { }

    override fun deob(source: Path, destination: Path) {
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
        logger.debug { "Fields removed: ${removedFields.size}" }
        writeJar(classNodes, destination)
    }
}