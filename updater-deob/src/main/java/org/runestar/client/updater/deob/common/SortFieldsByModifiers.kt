package org.runestar.client.updater.deob.common

import org.objectweb.asm.Type
import org.objectweb.asm.tree.FieldNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import java.lang.reflect.Modifier
import java.nio.file.Path

object SortFieldsByModifiers : Transformer {

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        classNodes.forEach { c ->
            c.fields = c.fields.sortedWith(FIELD_COMPARATOR)
        }
        writeJar(classNodes, destination)
    }

    private val FIELD_COMPARATOR: Comparator<FieldNode> = compareBy<FieldNode> { !Modifier.isStatic(it.access) }
            .thenBy { Modifier.toString(it.access and Modifier.fieldModifiers()) }
            .thenBy { Type.getType(it.desc).className }
            .thenBy { it.name }
}
