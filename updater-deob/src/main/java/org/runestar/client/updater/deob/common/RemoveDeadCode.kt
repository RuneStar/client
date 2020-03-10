package org.runestar.client.updater.deob.common

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.runestar.client.updater.deob.Transformer
import java.nio.file.Path

object RemoveDeadCode : Transformer.Tree() {

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        klasses.forEach { k ->
            k.methods.forEach { m ->
                try {
                    val frames = Analyzer(BasicInterpreter()).analyze(k.name, m)
                    val insns = m.instructions.toArray()
                    for (i in frames.indices) {
                        if (frames[i] == null) m.instructions.remove(insns[i])
                    }
                } catch (e: Exception) {
                    throw Exception("${k.name}.${m.name}${m.desc}", e)
                }
            }
        }
    }
}