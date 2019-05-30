package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.objectweb.asm.Type

@DependsOn(Node::class, ArchiveDisk::class)
class ArchiveDiskAction : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.any { it.type == type<ArchiveDisk>() } }

    @DependsOn(ArchiveDisk::class)
    class archiveDisk : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<ArchiveDisk>() }
    }

    @DependsOn(Archive::class)
    class archive : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Archive>() }
    }

    class data : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteArray::class.type }
    }

    class type : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.INT_TYPE }
    }
}