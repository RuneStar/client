package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2

@DependsOn(Actor::class)
class Npc : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Actor>() }
            .and { it.instanceFields.size == 1 }

    class type : InstanceField() {
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