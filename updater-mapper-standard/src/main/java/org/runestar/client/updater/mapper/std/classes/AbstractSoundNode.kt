package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Node::class, AbstractIntNode::class)
class AbstractSoundNode : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.any { it.type == type<AbstractIntNode>() } }

    class active : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE }
    }

    @DependsOn(AbstractSoundNode::class)
    class after : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<AbstractSoundNode>() }
    }

    @MethodParameters()
    @DependsOn(SoundNode2.firstNode::class)
    class firstNode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundNode2.firstNode>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundNode2.nextNode::class)
    class nextNode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundNode2.nextNode>().mark }
    }
}