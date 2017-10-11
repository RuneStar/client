package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import java.awt.Component
import java.awt.Image

@DependsOn(AbstractGraphicsProvider::class)
class GraphicsProvider : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<AbstractGraphicsProvider>() }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.any { it.type == Component::class.type } }

    class component : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Component::class.type }
    }

    @SinceVersion(141)
    class image : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Image::class.type }
    }

    @SinceVersion(141)
    @MethodParameters("x", "y", "width", "height")
    @DependsOn(AbstractGraphicsProvider.draw::class)
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractGraphicsProvider.draw>().mark }
    }

    @SinceVersion(141)
    @MethodParameters("x", "y")
    @DependsOn(AbstractGraphicsProvider.drawFull::class)
    class drawFull : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractGraphicsProvider.drawFull>().mark }
    }

    @SinceVersion(141)
    @MethodParameters("graphics", "x", "y", "width", "height")
    @DependsOn(draw::class)
    class draw0 : OrderMapper.InMethod.Method(draw::class, 0, 1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<GraphicsProvider>() }
    }

    @SinceVersion(141)
    @MethodParameters("graphics", "x", "y")
    @DependsOn(drawFull::class)
    class drawFull0 : OrderMapper.InMethod.Method(drawFull::class, 0, 1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<GraphicsProvider>() }
    }
}