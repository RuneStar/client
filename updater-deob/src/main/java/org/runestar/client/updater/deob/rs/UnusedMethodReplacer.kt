package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnNode
import org.runestar.client.updater.deob.Transformer
import java.lang.reflect.Modifier
import java.nio.file.Path

/**
 * Replaces the bodies of methods found by [UnusedMethodFinder] with `throw null;`
 */
object UnusedMethodReplacer : Transformer.Tree() {

    private val mapper = jacksonObjectMapper()

    private val logger = getLogger()

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        val unusedMethodNames: Set<String> = mapper.readValue(dir.resolve("unused-methods.json").toFile())

        klasses.forEach { c ->
            for (m in c.methods) {
                if (Modifier.isAbstract(m.access)) continue
                val mName: String = c.name + "." + m.name + m.desc
                if (mName !in unusedMethodNames) continue
                m.tryCatchBlocks.clear()
                m.instructions.clear()
                m.instructions.add(InsnNode(Opcodes.ACONST_NULL))
                m.instructions.add(InsnNode(Opcodes.ATHROW))
                m.maxLocals = Type.getMethodType(m.desc).argumentsAndReturnSizes shr 2
                if (!Modifier.isStatic(m.access)) m.maxLocals++
                m.maxStack = 1
            }
        }
        logger.info { "Unused methods replaced: ${unusedMethodNames.size}" }
    }
}