package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import java.lang.reflect.Modifier

@DependsOn(Component.rectangleMode::class)
class RectangleMode : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.type == field<Component.rectangleMode>().type }

    class id : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { Modifier.isPublic(it.access) }
    }

    class id2 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { !Modifier.isPublic(it.access) }
    }
}