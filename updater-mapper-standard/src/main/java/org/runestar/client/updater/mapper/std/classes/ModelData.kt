package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2

@DependsOn(Entity::class)
class ModelData : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Entity>() }
            .and { it.instanceFields.count { it.type == ShortArray::class.type } >= 8 }
}