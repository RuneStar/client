package org.runestar.client.updater.deob.rs

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.loggerfactory.getLogger
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.readJar
import org.runestar.client.updater.deob.writeJar
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Path

object UnusedMethodReplacer : Transformer {

    private val mapper = jacksonObjectMapper()

    private val logger = getLogger()

    override fun transform(source: Path, destination: Path) {
        val classNodes: Collection<ClassNode> = readJar(source)

        val unusedMethodsFile: Path = source.resolveSibling(source.fileName.toString() + ".unused-methods.json")
        check(Files.exists(unusedMethodsFile))

        val unusedMethodNames: Set<String> = mapper.readValue<Set<String>>(unusedMethodsFile.toFile())

        classNodes.forEach { c ->
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

        writeJar(classNodes, destination)
    }
}