package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2

@DependsOn(AbstractSoundNode::class)
class SoundNode4 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<AbstractSoundNode>() }
            .and { it.instanceFields.size == 15 }
}