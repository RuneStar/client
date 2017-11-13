package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@DependsOn(PacketBuffer.isaacCipher0::class)
class IsaacCipher : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.type == field<PacketBuffer.isaacCipher0>().type }
}