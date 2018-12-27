package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2

@DependsOn(Client.NetCache_indexCaches::class)
class NetCache : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { field<Client.NetCache_indexCaches>().klass == it }
}