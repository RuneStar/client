package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2

// https://en.wikipedia.org/wiki/Indexed_color
@DependsOn(Rasterizer2D::class)
class IndexedSprite : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Rasterizer2D>() }
            .and { it.instanceFields.count { it.type == ByteArray::class.type } == 1 }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 1 }

    @MethodParameters()
    class normalize : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
    }

    class palette : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == IntArray::class.type }
    }

    class pixels : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteArray::class.type }
    }

    @DependsOn(normalize::class)
    class width : OrderMapper.InMethod.Field(normalize::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldOwner == type<IndexedSprite>() }
    }

    @DependsOn(normalize::class)
    class height : OrderMapper.InMethod.Field(normalize::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldOwner == type<IndexedSprite>() }
    }

    @DependsOn(normalize::class)
    class xOffset : OrderMapper.InMethod.Field(normalize::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldOwner == type<IndexedSprite>() }
    }

    @DependsOn(normalize::class)
    class yOffset : OrderMapper.InMethod.Field(normalize::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldOwner == type<IndexedSprite>() }
    }

    @DependsOn(normalize::class)
    class subWidth : OrderMapper.InMethod.Field(normalize::class, -4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldOwner == type<IndexedSprite>() }
    }

    @DependsOn(normalize::class)
    class subHeight : OrderMapper.InMethod.Field(normalize::class, -3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldOwner == type<IndexedSprite>() }
    }

    @MethodParameters("r", "g", "b")
    class shiftColors : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size == 3 }
                .and { it.arguments.all { it == INT_TYPE } }
    }
}