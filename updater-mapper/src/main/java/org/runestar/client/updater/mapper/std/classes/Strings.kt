package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2

class Strings : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.staticFields.count { it.type == String::class.type } > 20 }
}