//package org.runestar.client.updater.mapper.std.classes
//
//import org.runestar.client.updater.mapper.IdentityMapper
//import org.runestar.client.updater.mapper.OrderMapper
//import org.runestar.client.updater.mapper.annotations.DependsOn
//import org.runestar.client.updater.mapper.annotations.MethodParameters
//import org.runestar.client.updater.mapper.extensions.and
//import org.runestar.client.updater.mapper.extensions.predicateOf
//import org.runestar.client.updater.mapper.extensions.type
//import org.runestar.client.updater.mapper.tree.Class2
//import org.runestar.client.updater.mapper.tree.Instruction2
//import org.runestar.client.updater.mapper.tree.Method2
//import org.objectweb.asm.Opcodes.PUTFIELD
//import org.objectweb.asm.Type
//import org.objectweb.asm.Type.INT_TYPE
//
//@DependsOn(DualNode::class)
//class Message : IdentityMapper.Class() {
//    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
//            .and { it.instanceFields.size == 6 }
//            .and { it.instanceFields.count { it.type == INT_TYPE } == 3 }
//            .and { it.instanceFields.count { it.type == String::class.type } == 3 }
//
//    class sender : OrderMapper.InConstructor.Field(Message::class, 0) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
//    }
//
//    class prefix : OrderMapper.InConstructor.Field(Message::class, 1) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
//    }
//
//    class text : OrderMapper.InConstructor.Field(Message::class, 2) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
//    }
//
//    class type : OrderMapper.InConstructor.Field(Message::class, 2, 3) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
//    }
//
//    class cycle : OrderMapper.InConstructor.Field(Message::class, 1, 3) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
//    }
//
//    class count : OrderMapper.InConstructor.Field(Message::class, 0, 3) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
//    }
//
//    @MethodParameters("type", "sender", "prefix", "text")
//    class set : IdentityMapper.InstanceMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE }
//    }
//}