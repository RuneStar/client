package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import java.lang.reflect.Modifier

@DependsOn(TileLocation::class)
class WorldMapSection : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { Modifier.isInterface(it.access) }
            .and { it.instanceMethods.any { it.returnType == type<TileLocation>() } }
}