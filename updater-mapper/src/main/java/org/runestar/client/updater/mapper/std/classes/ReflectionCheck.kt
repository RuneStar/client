package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.NEW
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.BYTE_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.AllUniqueMapper
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions
import org.runestar.client.updater.mapper.nextWithin
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2

@DependsOn(Node::class)
class ReflectionCheck : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.any { it.type == Array<java.lang.reflect.Field>::class.type } }
            .and { it.instanceFields.any { it.type == Array<java.lang.reflect.Method>::class.type } }

    class fields : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<java.lang.reflect.Field>::class.type }
    }

    class methods : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<java.lang.reflect.Method>::class.type }
    }

    class arguments : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BYTE_TYPE.withDimensions(3) }
    }

    class size : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<ReflectionCheck>() }
                .nextWithin(15) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class id : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<ReflectionCheck>() }
                .nextWithin(15) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class operations : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<ReflectionCheck>() }
                .nextWithin(15) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class creationErrors : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<ReflectionCheck>() }
                .nextWithin(15) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class intReplaceValues : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<ReflectionCheck>() }
                .nextWithin(15) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
                .nextWithin(10) { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
                .nextWithin(20) { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }
}