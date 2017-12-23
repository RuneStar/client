package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.mark
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import java.awt.Component

class Canvas : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == java.awt.Canvas::class.type }

    class component : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Component::class.type }
    }

    @MethodParameters("g")
    class paint : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == java.awt.Canvas::paint.mark }
    }

    @MethodParameters("g")
    class update : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == java.awt.Canvas::update.mark }
    }
}