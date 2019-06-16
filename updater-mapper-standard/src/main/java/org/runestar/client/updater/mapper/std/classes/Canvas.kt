package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.mark
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import java.awt.Canvas
import java.awt.Component

class Canvas : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Canvas::class.type }

    class component : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Component::class.type }
    }

    @MethodParameters("g")
    class paint : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == Canvas::paint.mark }
    }

    @MethodParameters("g")
    class update : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == Canvas::update.mark }
    }
}