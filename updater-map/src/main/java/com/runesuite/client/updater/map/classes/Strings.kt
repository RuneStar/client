package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2

class Strings : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.staticFields.count { it.type == String::class.type } > 20 }
}