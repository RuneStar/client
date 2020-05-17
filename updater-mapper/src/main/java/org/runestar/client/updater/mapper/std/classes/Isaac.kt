package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2

@DependsOn(PacketBit.isaac0::class)
class Isaac : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.type == field<PacketBit.isaac0>().type }
}