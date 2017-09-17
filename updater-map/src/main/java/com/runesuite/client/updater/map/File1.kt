package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

@DependsOn(AccessFile::class)
class File1 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<AccessFile>() } == 1 }

    @DependsOn(AccessFile::class)
    class file : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<AccessFile>() }
    }
}