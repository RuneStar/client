package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.*
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
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

    @MethodParameters("s")
    class stringWidth : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "img=" } }
    }

    @DependsOn(stringWidth::class)
    class charWidths : OrderMapper.InMethod.Field(stringWidth::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == IntArray::class.type }
    }

    @MethodParameters("c")
    class charWidth : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(CHAR_TYPE) }
    }

    @MethodParameters("color", "shadow")
    class reset : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("s", "lineWidths", "linesDst")
    class breakLines : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 3..4 }
                .and { it.arguments.startsWith(String::class.type, IntArray::class.type, String::class.type.withDimensions(1)) }
    }

    @MethodParameters("s", "lineWidth")
    class lineWidth : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(String::class.type, INT_TYPE) }
                .and { it.instructions.any { it.opcode == IINC } }
    }

    @MethodParameters("s", "lineWidth")
    class lineCount : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(String::class.type, INT_TYPE) }
                .and { it.instructions.none { it.opcode == IINC } }
    }
}