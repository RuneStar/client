package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.extensions.withDimensions
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Node::class)
class MusicPatch : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == ByteArray::class.type } == 3 }
            .and { it.instanceFields.count { it.type == ShortArray::class.type } == 1 }

    @MethodParameters()
    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == Type.VOID_TYPE }
    }

    @DependsOn(RawSound::class)
    class rawSounds : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<RawSound>().withDimensions(1) }
    }
}