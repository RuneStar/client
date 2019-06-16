package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.std.SpriteIdsField
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Method2

class GraphicsDefaults : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.size >= 11 }
            .and { it.instanceFields.all { it.type == Type.INT_TYPE } }
            .and { it.instanceMethods.size == 1 }

    @MethodParameters("archive")
    class decode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { true }
    }

    class compass : SpriteIdsField(0)
    class mapedge : SpriteIdsField(1)
    class mapscene : SpriteIdsField(2)
    class headiconspk : SpriteIdsField(3)
    class headiconsprayer : SpriteIdsField(4)
    class headiconshint : SpriteIdsField(5)
    class mapmarker : SpriteIdsField(6)
    class cross : SpriteIdsField(7)
    class mapdots : SpriteIdsField(8)
    class scrollbar : SpriteIdsField(9)
    class modicons : SpriteIdsField(10)
}