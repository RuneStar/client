//package org.runestar.client.updater.mapper.std.classes
//
//import org.objectweb.asm.Opcodes.*
//import org.runestar.client.updater.mapper.Class2
//import org.runestar.client.updater.mapper.DependsOn
//import org.runestar.client.updater.mapper.IdentityMapper
//import org.runestar.client.updater.mapper.and
//import org.runestar.client.updater.mapper.predicateOf
//
//@DependsOn(Enumerated::class)
//class Language : IdentityMapper.Class() {
//
//    override val predicate = predicateOf<Class2> { it.interfaces.contains(type<Enumerated>()) }
//            .and { it.classInitializer?.instructions?.any { it.opcode == LDC && it.ldcCst == "Spanish" } ?: false }
//}