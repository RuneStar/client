package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

// https://en.wikipedia.org/wiki/Indexed_color
@DependsOn(Rasterizer2D::class)
class IndexedSprite : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Rasterizer2D>() }
            .and { it.instanceFields.count { it.type == ByteArray::class.type } == 1 }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 1 }

    class palette : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == IntArray::class.type }
    }

    class pixels : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteArray::class.type }
    }
}