//package org.runestar.client.updater.mapper.std.classes
//
//import org.runestar.client.updater.mapper.IdentityMapper
//import org.runestar.client.updater.mapper.annotations.DependsOn
//import org.runestar.client.updater.mapper.extensions.and
//import org.runestar.client.updater.mapper.extensions.predicateOf
//import org.runestar.client.updater.mapper.extensions.type
//import org.runestar.client.updater.mapper.tree.Class2
//import org.runestar.client.updater.mapper.tree.Field2
//
//@DependsOn(WorldMapLabel::class, TileLocation::class)
//class WorldMapIcon : IdentityMapper.Class() {
//
//    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
//            .and { it.instanceFields.count { it.type == type<TileLocation>() } == 2 }
//
//    class label : IdentityMapper.InstanceField() {
//        override val predicate = predicateOf<Field2> { it.type == type<WorldMapLabel>() }
//    }
//}