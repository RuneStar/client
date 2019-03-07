//package org.runestar.client.updater.mapper.std.classes
//
//import org.objectweb.asm.Type
//import org.runestar.client.updater.mapper.IdentityMapper
//import org.runestar.client.updater.mapper.annotations.DependsOn
//import org.runestar.client.updater.mapper.extensions.and
//import org.runestar.client.updater.mapper.extensions.predicateOf
//import org.runestar.client.updater.mapper.extensions.type
//import org.runestar.client.updater.mapper.tree.Class2
//
//@DependsOn(Enumerated::class)
//class OctantDirection : IdentityMapper.Class() {
//
//    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
//            .and { it.interfaces == listOf(type<Enumerated>()) }
//            .and { it.instanceFields.size == 2 }
//            .and { it.instanceFields.count { it.type == Type.INT_TYPE } == 2 }
//            .and { it.instanceMethods.size == 1 }
//            .and { c -> c.staticFields.count { it.type == c.type } == 8 }
//}