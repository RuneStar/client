package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Instruction2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE

class World : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == INT_TYPE } >= 5 }
            .and { it.instanceFields.count { it.type == String::class.type } == 2 }
            .and { it.instanceFields.all { it.type == String::class.type || it.type == INT_TYPE } }

    @DependsOn(Client.loadWorlds::class)
    class id : OrderMapper.InMethod.Field(Client.loadWorlds::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.loadWorlds::class)
    class properties : OrderMapper.InMethod.Field(Client.loadWorlds::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.loadWorlds::class)
    class location : OrderMapper.InMethod.Field(Client.loadWorlds::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.loadWorlds::class)
    class population : OrderMapper.InMethod.Field(Client.loadWorlds::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.loadWorlds::class)
    class index : OrderMapper.InMethod.Field(Client.loadWorlds::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.loadWorlds::class)
    class host : OrderMapper.InMethod.Field(Client.loadWorlds::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    @DependsOn(Client.loadWorlds::class)
    class activity : OrderMapper.InMethod.Field(Client.loadWorlds::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }
}