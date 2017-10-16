package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@SinceVersion(141)
@DependsOn(ScriptVarType::class, Enumerated::class)
class ScriptVarType0 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<Enumerated>() != it }
            .and { klass<ScriptVarType>().interfaces.contains(it.type) }
}