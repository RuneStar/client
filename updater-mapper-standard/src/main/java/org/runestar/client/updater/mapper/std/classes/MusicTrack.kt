package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2

@DependsOn(Node::class, NodeHashTable::class)
class MusicTrack : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == ByteArray::class.type } == 1 }
            .and { it.instanceFields.count { it.type == type<NodeHashTable>() } == 1 }

    class midi : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteArray::class.type }
    }

    @DependsOn(NodeHashTable::class)
    class table : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<NodeHashTable>() }
    }

    @MethodParameters()
    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == Type.VOID_TYPE }
                .and { it.instructions.count() < 20 }
    }
}