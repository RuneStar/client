package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2

@DependsOn(Client.Tiles_renderFlags::class)
class Tiles : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it == field<Client.Tiles_renderFlags>().klass }
}