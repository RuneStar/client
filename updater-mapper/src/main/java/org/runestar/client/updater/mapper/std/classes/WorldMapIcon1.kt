package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2

@DependsOn(AbstractWorldMapIcon::class)
class WorldMapIcon1 : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<AbstractWorldMapIcon>() }
            .and { it.instanceFields.size == 4 }

    @DependsOn(WorldMapLabel::class)
    class label0 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<WorldMapLabel>() }
    }
}