package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.IDIV
import org.objectweb.asm.Opcodes.IINC
import org.objectweb.asm.Opcodes.ISUB
import org.objectweb.asm.Opcodes.LDC
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.CHAR_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import org.runestar.client.common.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import java.lang.reflect.Modifier

@DependsOn(Rasterizer2D::class)
class AbstractFont : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { Modifier.isAbstract(it.access) }
            .and { it.superType == type<Rasterizer2D>() }

    class pixels  : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<ByteArray>::class.type }
    }

    // always null?
    class kerning  : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteArray::class.type }
    }

    @MethodParameters("s")
    class decodeTag : IdentityMapper.InstanceMethod() {
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

    @DependsOn(stringWidth::class)
    class advances : OrderMapper.InMethod.Field(stringWidth::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == IntArray::class.type }
    }

    class widths : OrderMapper.InConstructor.Field(AbstractFont::class, 2) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size > 2 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class heights : OrderMapper.InConstructor.Field(AbstractFont::class, 3) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size > 2 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class leftBearings : OrderMapper.InConstructor.Field(AbstractFont::class, 0) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size > 2 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class topBearings : OrderMapper.InConstructor.Field(AbstractFont::class, 1) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size > 2 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    // p11: this = 10, max descent = 3, height = 12, max ascent = 9, main ascent = 8
    // p12: this = 12, max descent = 4, height = 16, max ascent = 12, main ascent = 11
    class ascent : OrderMapper.InConstructor.Field(AbstractFont::class, 0) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size > 2 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    // p11: 10
    // p12: 12
    class maxAscent : OrderMapper.InConstructor.Field(AbstractFont::class, -2) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size > 2 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    // p11: 2
    // p12: 4
    class maxDescent : OrderMapper.InConstructor.Field(AbstractFont::class, -1) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size > 2 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters("pixels", "x", "y", "width", "height", "color")
    class drawGlyph : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { Modifier.isAbstract(it.access) }
                .and { it.arguments == listOf(ByteArray::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("pixels", "x", "y", "width", "height", "color", "alpha")
    class drawGlyphAlpha : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { Modifier.isAbstract(it.access) }
                .and { it.arguments == listOf(ByteArray::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("s", "x", "y")
    class draw0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("s", "x", "y", "color", "shadow")
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.none { it.opcode == ISUB } }
    }

    @MethodParameters("s", "x", "y", "color", "shadow")
    class drawRightAligned : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == ISUB } }
                .and { it.instructions.none { it.opcode == IDIV } }
    }

    @MethodParameters("s", "x", "y", "color", "shadow")
    class drawCentered : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == ISUB } }
                .and { it.instructions.any { it.opcode == IDIV } }
    }

    @MethodParameters("s", "x", "y", "color", "shadow", "alpha")
    class drawAlpha : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.none { it.opcode == IINC } }
    }

    @MethodParameters("s", "x", "y", "xs", "ys")
    class drawWithOffsets0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE, IntArray::class.type, IntArray::class.type) }
    }

    @MethodParameters("s", "x", "y", "width", "height", "color", "shadow", "xAlignment", "yAlignment", "lineHeight")
    class drawLines : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("s", "lineWidth")
    class calculateLineJustification : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE) }
    }

    @MethodParameters("s", "x", "y", "color", "shadow", "seed")
    class drawRandomAlphaAndSpacing : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.isMethod && it.methodName == "setSeed" } }
    }

    @MethodParameters("s", "x", "y", "color", "shadow", "seed")
    class drawCenteredWave2 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == 3.0 } }
    }

    @MethodParameters("s", "x", "y", "color", "shadow", "seed")
    class drawCenteredWave : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == 2.0 } }
    }

    @MethodParameters("s", "x", "y", "color", "shadow", "seed", "seed2")
    class drawCenteredShake : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("bytes")
    class readMetrics : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE && it.arguments == listOf(ByteArray::class.type) }
    }
}