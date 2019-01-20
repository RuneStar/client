package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2

@DependsOn(WorldMapArea::class)
class WorldMapAreaData : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<WorldMapArea>() }
}