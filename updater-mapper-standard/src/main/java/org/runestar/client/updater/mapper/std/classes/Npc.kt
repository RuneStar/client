package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Actor::class)
class Npc : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Actor>() }
            .and { it.instanceFields.size == 1 }

    class definition : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type in it.jar }
    }

    @MethodParameters
    @DependsOn(Actor.isVisible::class)
    class isVisible : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Actor.isVisible>().mark }
    }

    @MethodParameters
    @DependsOn(Entity.getModel::class)
    class getModel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Entity.getModel>().mark }
    }
}