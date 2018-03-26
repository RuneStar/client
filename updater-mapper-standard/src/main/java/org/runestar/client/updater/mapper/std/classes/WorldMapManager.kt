package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.*
import org.runestar.client.updater.mapper.tree.Class2

@DependsOn(IndexedSprite::class)
class WorldMapManager : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.constructors.isNotEmpty() }
            .and { it.constructors.first().arguments == listOf(type<IndexedSprite>().withDimensions(1), HashMap::class.type) }
}