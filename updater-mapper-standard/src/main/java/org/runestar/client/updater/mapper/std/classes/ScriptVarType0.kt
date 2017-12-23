package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2

@SinceVersion(141)
@DependsOn(ScriptVarType::class, Enumerated::class)
class ScriptVarType0 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<Enumerated>() != it }
            .and { klass<ScriptVarType>().interfaces.contains(it.type) }
}