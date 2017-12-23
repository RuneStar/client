package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Node::class)
class WidgetNode : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == BOOLEAN_TYPE } == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 2 }

    @DependsOn(NodeHashTable.first::class)
    class type : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL && it.methodId == method<NodeHashTable.first>().id }
                .nextIn(1) { it.opcode == CHECKCAST && it.typeType == type<WidgetNode>() }
                .nextWithin(23) { it.opcode == ICONST_3 }
                .prevWithin(10) { it.opcode == GETFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<WidgetNode>() }
    }

    @DependsOn(type::class)
    class id : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE && it != field<type>() }
    }

    class boolean1 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE }
    }
}