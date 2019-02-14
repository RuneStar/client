package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Node::class, ObjectDefinition::class)
class ObjectSound : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.any { it.type == type<ObjectDefinition>() } }

    @DependsOn(ObjectDefinition::class)
    class obj : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<ObjectDefinition>() }
    }

    @MethodParameters()
    class set : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { true }
    }

    @DependsOn(set::class)
    class soundEffectId : OrderMapper.InMethod.Field(set::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    class soundEffectIds : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == IntArray::class.type }
    }
}