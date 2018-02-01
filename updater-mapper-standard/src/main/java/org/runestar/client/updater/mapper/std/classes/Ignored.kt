//package org.runestar.client.updater.mapper.std.classes
//
//import org.runestar.client.updater.mapper.IdentityMapper
//import org.runestar.client.updater.mapper.StaticUniqueMapper
//import org.runestar.client.updater.mapper.annotations.DependsOn
//import org.runestar.client.updater.mapper.extensions.and
//import org.runestar.client.updater.mapper.extensions.predicateOf
//import org.runestar.client.updater.mapper.extensions.type
//import org.runestar.client.updater.mapper.nextWithin
//import org.runestar.client.updater.mapper.tree.Class2
//import org.runestar.client.updater.mapper.tree.Field2
//import org.runestar.client.updater.mapper.tree.Instruction2
//import org.objectweb.asm.Opcodes
//
//class Ignored : IdentityMapper.Class() {
//    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
//            .and { it.instanceMethods.isEmpty() }
//            .and { it.instanceFields.count { it.type == String::class.type } == 2 }
//            .and { it.instanceFields.size == 2 }
//            .and { it.constructors.all { it.arguments.isEmpty() } }
//
//    class name : StaticUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.SIPUSH && it.intOperand == 3622 }
//                .nextWithin(50) { it.opcode == Opcodes.GETFIELD && it.fieldOwner == type<Ignored>() && it.fieldType == String::class.type }
//    }
//
//    @DependsOn(name::class)
//    class previousName : InstanceField() {
//        override val predicate = predicateOf<Field2> { it.type == String::class.type && it != field<name>() }
//    }
//}