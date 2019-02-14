package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(AbstractSoundNode::class)
class SoundNode2 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<AbstractSoundNode>() }
            .and { it.instanceFields.size == 4 }

    @MethodParameters()
    @DependsOn(NodeDeque.last::class)
    class firstNode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<AbstractSoundNode>() }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.isMethod && it.methodId == method<NodeDeque.last>().id } }
    }

    @MethodParameters()
    @DependsOn(NodeDeque.previous::class)
    class nextNode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<AbstractSoundNode>() }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.isMethod && it.methodId == method<NodeDeque.previous>().id } }
    }
}