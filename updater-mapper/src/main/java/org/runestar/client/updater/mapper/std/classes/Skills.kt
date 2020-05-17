package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2

@DependsOn(Client.Skills_enabled::class)
class Skills : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { field<Client.Skills_enabled>().klass == it }
}