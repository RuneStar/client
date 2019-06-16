package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2

@DependsOn(Client.NetCache_archives::class)
class NetCache : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { field<Client.NetCache_archives>().klass == it }
}