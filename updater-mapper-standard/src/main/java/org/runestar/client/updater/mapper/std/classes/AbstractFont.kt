package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import java.lang.reflect.Modifier

@DependsOn(Rasterizer2D::class)
class AbstractFont : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { Modifier.isAbstract(it.access) }
            .and { it.superType == type<Rasterizer2D>() }

    class glyphs : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<ByteArray>::class.type }
    }

    class decodeColor : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "/shad" } }
    }
}