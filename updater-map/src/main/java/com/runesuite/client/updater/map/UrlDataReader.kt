//package com.runesuite.updater.map.classmappers
//
//import com.runesuite.mapper.IdentityMapper
//import com.runesuite.mapper.tree.Class2
//import com.runesuite.mapper.tree.Field2
//import com.runesuite.mapper.tree.Method2
//import extensions.and
//import extensions.predicateOf
//import extensions.type
//import java.io.DataInputStream
//
//class UrlDataReader : IdentityMapper.Class() {
//    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
//            .and { it.instanceFields.any { it.type == DataInputStream::class.type } }
//
//    class read : IdentityMapper.InstanceMethod() {
//        override val predicate = predicateOf<Method2> { true }
//    }
//
//    class dataInputStream : IdentityMapper.InstanceField() {
//        override val predicate = predicateOf<Field2> { it.type == DataInputStream::class.type }
//    }
//}