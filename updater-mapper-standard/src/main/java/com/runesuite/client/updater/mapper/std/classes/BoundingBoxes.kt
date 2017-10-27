package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@DependsOn(Client.boundingBoxes::class)
class BoundingBoxes : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it == field<Client.boundingBoxes>().klass }
}