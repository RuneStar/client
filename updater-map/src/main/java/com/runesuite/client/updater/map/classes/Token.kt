package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@SinceVersion(141)
@DependsOn(IntegerToken::class)
class Token : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<IntegerToken>().interfaces.contains(it.type) }
}