package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.std.SpriteSetSprite
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2

@SinceVersion(173)
class SpriteSet : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.size >= 11 }
            .and { it.instanceFields.all { it.type == Type.INT_TYPE } }
            .and { it.instanceMethods.size == 1 }

    @MethodParameters("index")
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { true }
    }

    // archiveIds
    class headIconsPk : SpriteSetSprite(3)
    class headIconsPrayer : SpriteSetSprite(4)
    class headIconsHint : SpriteSetSprite(5)
    class mapMarkers : SpriteSetSprite(6)
    class crosses : SpriteSetSprite(7)
    class mapDots : SpriteSetSprite(8)
}