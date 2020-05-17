package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.objectweb.asm.Type.*

@DependsOn(ClientProt0::class)
class LoginProt : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.interfaces.contains(type<ClientProt0>()) }
            .and { it.instanceFields.size == 1 }

    class id : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }
}