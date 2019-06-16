package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.withDimensions
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2

@DependsOn(DualNode::class, AnimFrame::class)
class AnimFrameset : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.size == 1 }
            .and { it.instanceFields.any { it.type == type<AnimFrame>().withDimensions(1) } }

    @DependsOn(AnimFrame::class)
    class frames : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<AnimFrame>().withDimensions(1) }
    }

    @MethodParameters("frame")
    class hasAlphaTransform : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
    }
}