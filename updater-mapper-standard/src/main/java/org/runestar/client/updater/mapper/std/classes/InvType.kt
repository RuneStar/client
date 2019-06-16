package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.ICONST_2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import java.lang.reflect.Modifier

@DependsOn(DualNode::class)
class InvType : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.size == 1 }
            .and { it.instanceFields.all { it.type == INT_TYPE } }
            .and { !Modifier.isAbstract(it.access) }
            .and { it.instanceMethods.any { it.instructions.any { it.opcode == ICONST_2 } } }

    class size : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { true }
    }

    @DependsOn(Packet::class)
    class decode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.none { it.opcode == PUTFIELD } }
    }

    @DependsOn(Packet::class)
    class decode0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.any { it.opcode == PUTFIELD } }
    }
}