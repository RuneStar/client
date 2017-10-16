package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@DependsOn(Client.Bzip2Decompressor_state::class)
class Bzip2Decompressor : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { field<Client.Bzip2Decompressor_state>().klass == it }
}