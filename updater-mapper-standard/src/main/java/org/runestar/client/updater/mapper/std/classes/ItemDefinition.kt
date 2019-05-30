package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.prevIn
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(DualNode::class)
class ItemDefinition : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.count { it.type == ShortArray::class.type } == 4 }
            .and { it.instanceFields.count { it.type == Array<String>::class.type } == 2 }

    @MethodParameters("quantity")
    @DependsOn(Model::class)
    class getModel : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Model>() }
    }

    class name : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }

    class op : OrderMapper.InConstructor.Field(ItemDefinition::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == Array<String>::class.type }
    }

    class iop : OrderMapper.InConstructor.Field(ItemDefinition::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == Array<String>::class.type }
    }

    class members : OrderMapper.InConstructor.Field(ItemDefinition::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == BOOLEAN_TYPE }
    }

    class stockmarket : OrderMapper.InConstructor.Field(ItemDefinition::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(Buffer::class)
    class decode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Buffer>()) }
                .and { it.instructions.none { it.opcode == BIPUSH && it.intOperand == 16 } }
    }

    @DependsOn(Buffer::class)
    class decode0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Buffer>()) }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 16 } }
    }

    @DependsOn(Client.getItemDefinition::class)
    class id : OrderMapper.InMethod.Field(Client.getItemDefinition::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<ItemDefinition>() }
    }

    class zoom2d : OrderMapper.InConstructor.Field(ItemDefinition::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class xan2d : OrderMapper.InConstructor.Field(ItemDefinition::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class yan2d : OrderMapper.InConstructor.Field(ItemDefinition::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class zan2d : OrderMapper.InConstructor.Field(ItemDefinition::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class xof2d : OrderMapper.InConstructor.Field(ItemDefinition::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class yof2d : OrderMapper.InConstructor.Field(ItemDefinition::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class stackable : OrderMapper.InConstructor.Field(ItemDefinition::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class cost : OrderMapper.InConstructor.Field(ItemDefinition::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class shiftclickindex : OrderMapper.InConstructor.Field(ItemDefinition::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class manwear : OrderMapper.InConstructor.Field(ItemDefinition::class, 9) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class manwear2 : OrderMapper.InConstructor.Field(ItemDefinition::class, 10) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class manwearyoff : OrderMapper.InConstructor.Field(ItemDefinition::class, 11) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class womanwear : OrderMapper.InConstructor.Field(ItemDefinition::class, 12) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class womanwear2 : OrderMapper.InConstructor.Field(ItemDefinition::class, 13) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class womanwearyoff : OrderMapper.InConstructor.Field(ItemDefinition::class, 14) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class manwear3 : OrderMapper.InConstructor.Field(ItemDefinition::class, 15) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class womanwear3 : OrderMapper.InConstructor.Field(ItemDefinition::class, 16) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class manhead : OrderMapper.InConstructor.Field(ItemDefinition::class, 17) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class manhead2 : OrderMapper.InConstructor.Field(ItemDefinition::class, 18) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class womanhead : OrderMapper.InConstructor.Field(ItemDefinition::class, 19) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class womanhead2 : OrderMapper.InConstructor.Field(ItemDefinition::class, 20) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class certlink : OrderMapper.InConstructor.Field(ItemDefinition::class, 21) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class certtemplate : OrderMapper.InConstructor.Field(ItemDefinition::class, 22) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class resizex : OrderMapper.InConstructor.Field(ItemDefinition::class, 23) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class resizey : OrderMapper.InConstructor.Field(ItemDefinition::class, 24) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class resizez : OrderMapper.InConstructor.Field(ItemDefinition::class, 25) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class ambient : OrderMapper.InConstructor.Field(ItemDefinition::class, 26) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class contrast : OrderMapper.InConstructor.Field(ItemDefinition::class, 27) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class team : OrderMapper.InConstructor.Field(ItemDefinition::class, 28) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class boughtlink : OrderMapper.InConstructor.Field(ItemDefinition::class, 29) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class boughttemplate : OrderMapper.InConstructor.Field(ItemDefinition::class, 30) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class placeholderlink : OrderMapper.InConstructor.Field(ItemDefinition::class, 31) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    class placeholdertemplate : OrderMapper.InConstructor.Field(ItemDefinition::class, 32) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == INT_TYPE }
    }

    @DependsOn(getModel::class)
    class recol_s : OrderMapper.InMethod.Field(getModel::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(getModel::class)
    class recol_d : OrderMapper.InMethod.Field(getModel::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(getModel::class)
    class retex_s : OrderMapper.InMethod.Field(getModel::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(getModel::class)
    class retex_d : OrderMapper.InMethod.Field(getModel::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SALOAD }
                .prevIn(2) { it.opcode == GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(getModel::class)
    class model : OrderMapper.InMethod.Field(getModel::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(getModel::class)
    class countco : OrderMapper.InMethod.Field(getModel::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(getModel::class)
    class countobj : OrderMapper.InMethod.Field(getModel::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == IntArray::class.type }
    }

    @MethodParameters()
    class getShiftClickIndex : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
    }

    @DependsOn(IterableNodeHashTable::class)
    class params : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeHashTable>() }
    }

    class getIntParam : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE) }
    }

    class getStringParam : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
                .and { it.arguments == listOf(INT_TYPE, String::class.type) }
    }

    @MethodParameters("quantity")
    class getCountObj : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<ItemDefinition>() }
                .and { it.arguments == listOf(INT_TYPE) }
    }

    @MethodParameters("template", "link")
    @DependsOn(Client.getItemDefinition::class)
    class genCert : OrderMapper.InMethod.Method(Client.getItemDefinition::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<ItemDefinition>() &&
                it.methodType == Type.getMethodType(VOID_TYPE, type<ItemDefinition>(), type<ItemDefinition>()) }
    }

    @MethodParameters("template", "link")
    @DependsOn(Client.getItemDefinition::class)
    class genBought : OrderMapper.InMethod.Method(Client.getItemDefinition::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<ItemDefinition>() &&
                it.methodType == Type.getMethodType(VOID_TYPE, type<ItemDefinition>(), type<ItemDefinition>()) }
    }

    @MethodParameters("template", "link")
    @DependsOn(Client.getItemDefinition::class)
    class genPlaceholder : OrderMapper.InMethod.Method(Client.getItemDefinition::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<ItemDefinition>() &&
                it.methodType == Type.getMethodType(VOID_TYPE, type<ItemDefinition>(), type<ItemDefinition>()) }
    }
}