package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@DependsOn(AbstractWorldMapIcon::class)
class WorldMapIcon2 : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<AbstractWorldMapIcon>() }
            .and { it.instanceFields.size == 6 }

    class label : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<WorldMapLabel>() }
    }
}