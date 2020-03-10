package org.runestar.client.updater.deob.common

import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.runestar.client.updater.deob.Transformer
import java.lang.reflect.Modifier
import java.nio.file.Path

object SortFieldsByModifiers : Transformer.Tree() {

    override fun transform(dir: Path, klasses: List<ClassNode>) {
        klasses.forEach { k ->
            k.fields = k.fields.sortedWith(FIELD_COMPARATOR)
        }
    }

    private val FIELD_COMPARATOR: Comparator<FieldNode> = compareBy<FieldNode> { !Modifier.isStatic(it.access) }
            .thenBy { Modifier.toString(it.access and Modifier.fieldModifiers()) }
            .thenBy { Type.getType(it.desc).className }
            .thenBy { it.name }
}
