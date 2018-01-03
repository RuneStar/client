package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Entity::class)
class ModelData : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Entity>() }
            .and { it.instanceFields.count { it.type == ShortArray::class.type } >= 8 }

    class verticesCount : OrderMapper.InConstructor.Field(ModelData::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
        override val constructorPredicate = predicateOf<Method2> { it.arguments.isEmpty() }
    }

    class faceCount : OrderMapper.InConstructor.Field(ModelData::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
        override val constructorPredicate = predicateOf<Method2> { it.arguments.isEmpty() }
    }

    @MethodParameters("from", "to")
    class recolor : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(SHORT_TYPE, SHORT_TYPE) }
                .and { it.instructions.count { it.isField && it.fieldType == ShortArray::class.type } == 2 }
    }

    @MethodParameters("from", "to")
    class retexture : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(SHORT_TYPE, SHORT_TYPE) }
                .and { it.instructions.count { it.isField && it.fieldType == ShortArray::class.type } == 3 }
    }

    @DependsOn(recolor::class)
    class faceColors : UniqueMapper.InMethod.Field(recolor::class) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(retexture::class)
    class faceTextures : UniqueMapper.InMethod.Field(retexture::class) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == ShortArray::class.type }
    }
}