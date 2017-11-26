package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Rasterizer2D::class, Client.worldToMinimap::class)
class Sprite : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Rasterizer2D>() }
            .and { c -> method<Client.worldToMinimap>().arguments.first { it in c.jar } == c.type }

    class pixels : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == IntArray::class.type }
    }

    class width : OrderMapper.InConstructor.Field(Sprite::class, 0, 6) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size >= 3 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class subWidth : OrderMapper.InConstructor.Field(Sprite::class, 1, 6) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size >= 3 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class height : OrderMapper.InConstructor.Field(Sprite::class, 2, 6) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size >= 3 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class subHeight : OrderMapper.InConstructor.Field(Sprite::class, 3, 6) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size >= 3 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class xOffset : OrderMapper.InConstructor.Field(Sprite::class, 4, 6) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size >= 3 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class yOffset : OrderMapper.InConstructor.Field(Sprite::class, 5, 6) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size >= 3 }
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}