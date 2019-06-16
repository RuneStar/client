package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions
import org.runestar.client.updater.mapper.std.ScriptField
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2

@DependsOn(IterableNodeHashTable::class, DualNode::class)
class ClientScript : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.any { it.type == type<IterableNodeHashTable>().withDimensions(1) } }

    class stringOperands : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<String>::class.type }
    }

    @DependsOn(IterableNodeHashTable::class)
    class switches : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeHashTable>().withDimensions(1) }
    }

    class localIntCount : ScriptField(0, INT_TYPE)
    class localStringCount : ScriptField(1, INT_TYPE)
    class intArgumentCount : ScriptField(2, INT_TYPE)
    class stringArgumentCount : ScriptField(3, INT_TYPE)

    class opcodes : ScriptField( 0, IntArray::class.type)
    class intOperands : ScriptField( 1, IntArray::class.type)
}