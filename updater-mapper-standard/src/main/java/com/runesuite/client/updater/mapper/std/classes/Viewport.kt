package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@SinceVersion(141)
@DependsOn(Client.Viewport_containsMouse::class)
class Viewport : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { field<Client.Viewport_containsMouse>().klass == it }
}