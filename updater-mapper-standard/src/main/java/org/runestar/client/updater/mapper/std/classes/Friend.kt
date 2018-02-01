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
//import org.runestar.client.updater.mapper.tree.Instruction2
//import org.objectweb.asm.Opcodes.GETFIELD
//import org.objectweb.asm.Opcodes.SIPUSH
//import org.objectweb.asm.Type.BOOLEAN_TYPE
//import org.objectweb.asm.Type.INT_TYPE
//
//class Friend : IdentityMapper.Class() {
//    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
//            .and { it.instanceFields.count { it.type == String::class.type } == 2 }
//            .and { it.instanceFields.count { it.type == INT_TYPE } == 2 }
//            .and { it.instanceFields.count { it.type == BOOLEAN_TYPE } == 2 }
//            .and { it.instanceMethods.isEmpty() }
//
//    class name : StaticUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3601 }
//                .nextWithin(40) { it.opcode == GETFIELD && it.fieldOwner == type<Friend>() && it.fieldType == String::class.type }
//    }
//
//    @DependsOn(name::class)
//    class previousName : StaticUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3601 }
//                .nextWithin(40) { it.opcode == GETFIELD && it.fieldId == field<name>().id }
//                .nextWithin(20) { it.opcode == GETFIELD && it.fieldOwner == type<Friend>() && it.fieldType == String::class.type }
//    }
//
//    class world : StaticUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3602 }
//                .nextWithin(40) { it.opcode == GETFIELD && it.fieldOwner == type<Friend>() && it.fieldType == INT_TYPE }
//    }
//
//    class rank : StaticUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3603 }
//                .nextWithin(40) { it.opcode == GETFIELD && it.fieldOwner == type<Friend>() && it.fieldType == INT_TYPE }
//    }
//}