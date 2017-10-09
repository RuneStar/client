package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.StaticUniqueMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.nextIn
import com.runesuite.mapper.nextWithin
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Node::class)
class WidgetNode : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == BOOLEAN_TYPE } == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 2 }

    @DependsOn(NodeHashTable.first::class)
    @SinceVersion(155)
    class id : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL && it.methodId == method<NodeHashTable.first>().id }
                .nextIn(1) { it.opcode == CHECKCAST && it.typeType == type<WidgetNode>() }
                .nextWithin(13) { it.opcode == GETFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<WidgetNode>() }
    }

    @SinceVersion(155)
    @DependsOn(id::class)
    class type : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE && it != field<id>() }
    }

    class boolean1 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE }
    }
}