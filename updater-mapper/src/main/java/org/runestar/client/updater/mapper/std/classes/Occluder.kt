package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.std.OccluderField
import org.runestar.client.updater.mapper.Class2

class Occluder : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.size == 18 }
            .and { it.instanceFields.all { it.type == Type.INT_TYPE } }

    class minTileX : OccluderField(0)
    class maxTileX : OccluderField(1)
    class minTileY : OccluderField(2)
    class maxTileY : OccluderField(3)
    class type : OccluderField(4)
    class minX : OccluderField(5)
    class maxX : OccluderField(6)
    class minZ : OccluderField(7)
    class maxZ : OccluderField(8)
    class minY : OccluderField(9)
    class maxY : OccluderField(10)
}