package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2

@SinceVersion(141)
@DependsOn(IntegerToken::class)
class Token : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<IntegerToken>().interfaces.contains(it.type) }
}