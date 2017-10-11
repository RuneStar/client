package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.mark
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
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