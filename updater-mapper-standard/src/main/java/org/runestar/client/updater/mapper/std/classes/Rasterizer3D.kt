package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2

@DependsOn(Rasterizer2D::class)
class Rasterizer3D : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Rasterizer2D>() }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.isEmpty() }
            .and { it.instanceMethods.isEmpty() }
}