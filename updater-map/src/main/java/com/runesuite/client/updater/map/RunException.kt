package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2

class RunException : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == RuntimeException::class.type }
}