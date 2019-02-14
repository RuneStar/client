package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(AbstractSoundNode::class)
class SoundNode5 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<AbstractSoundNode>() }
            .and { it.instanceFields.size == 27 }

    @DependsOn(NodeHashTable::class)
    class table : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<NodeHashTable>() }
    }

    @MethodParameters()
    @DependsOn(Node.remove::class)
    class removeAll : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == Type.VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodMark == method<Node.remove>().mark } }
    }

    @MethodParameters()
    @DependsOn(MusicPatch.clear::class)
    class clearAll : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == Type.VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<MusicPatch.clear>().id } }
    }

    @DependsOn(TrackData::class)
    class trackData : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<TrackData>() }
    }
}