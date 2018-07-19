package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.*
import org.runestar.client.updater.mapper.std.Widget10Array
import org.runestar.client.updater.mapper.std.WidgetInvArray
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Node::class)
class Widget : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == Array<Any>::class.type } > 10 }

    class parent : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Widget>() }
    }

    class children : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Widget>().withDimensions(1) }
    }

    class hasScript : OrderMapper.InConstructor.Field(Widget::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class isScrollBar : OrderMapper.InConstructor.Field(Widget::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class noClickThrough : OrderMapper.InConstructor.Field(Widget::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class id : OrderMapper.InConstructor.Field(Widget::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class childIndex : OrderMapper.InConstructor.Field(Widget::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class buttonType : OrderMapper.InConstructor.Field(Widget::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class contentType : OrderMapper.InConstructor.Field(Widget::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class width : OrderMapper.InConstructor.Field(Widget::class, 14) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class height : OrderMapper.InConstructor.Field(Widget::class, 15) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class parentId : OrderMapper.InConstructor.Field(Widget::class, 18) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class isHidden : OrderMapper.InConstructor.Field(Widget::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class textColor : OrderMapper.InConstructor.Field(Widget::class, 23) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class textureId : OrderMapper.InConstructor.Field(Widget::class, 30) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class modelFrameCycle : OrderMapper.InConstructor.Field(Widget::class, -7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class modelFrame : OrderMapper.InConstructor.Field(Widget::class, -8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class itemQuantity : OrderMapper.InConstructor.Field(Widget::class, -9) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class itemId : OrderMapper.InConstructor.Field(Widget::class, -10) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class text : OrderMapper.InConstructor.Field(Widget::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    class columns : OrderMapper.InConstructor.Field(Widget::class, 10) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class rows : OrderMapper.InConstructor.Field(Widget::class, 11) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class x : OrderMapper.InConstructor.Field(Widget::class, 12) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class y : OrderMapper.InConstructor.Field(Widget::class, 13) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class rootIndex : OrderMapper.InConstructor.Field(Widget::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class scrollX : OrderMapper.InConstructor.Field(Widget::class, 19) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class scrollY : OrderMapper.InConstructor.Field(Widget::class, 20) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class cycle : OrderMapper.InConstructor.Field(Widget::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class paddingX : OrderMapper.InConstructor.Field(Widget::class, -16) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class paddingY : OrderMapper.InConstructor.Field(Widget::class, -15) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class clickMask : OrderMapper.InConstructor.Field(Widget::class, -14) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class dragThreshold : OrderMapper.InConstructor.Field(Widget::class, -12) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    // ok, select, continue
    class buttonText : OrderMapper.InConstructor.Field(Widget::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    class scrollMax : OrderMapper.InConstructor.Field(Widget::class, 22) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class spellName : OrderMapper.InConstructor.Field(Widget::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    // "Cast"
    class spellActionName : OrderMapper.InConstructor.Field(Widget::class, -3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    // not always displayed, includes color tags, bank item names, spell names, quest list names
    class dataText : OrderMapper.InConstructor.Field(Widget::class, -4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    class string1 : OrderMapper.InConstructor.Field(Widget::class, -5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == String::class.type }
    }

    @MethodParameters()
    @DependsOn(Font::class)
    class getFont : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Font>() }
    }

    @DependsOn(getFont::class)
    class fontId : OrderMapper.InMethod.Field(getFont::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters("sequence", "frame", "b", "appearance")
    @DependsOn(Model::class)
    class getModel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Model>() }
    }

//    @DependsOn(getModel::class)
//    class modelType : OrderMapper.InMethod.Field(getModel::class, 0) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
//    }
//
//    @DependsOn(getModel::class)
//    class modelId : OrderMapper.InMethod.Field(getModel::class, 1) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
//    }

    @MethodParameters("index", "s")
    class setAction : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, String::class.type) }
    }

    @DependsOn(setAction::class)
    class actions : UniqueMapper.InMethod.Field(setAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == String::class.type.withDimensions(1) }
    }

    @DependsOn(Buffer::class, Client.Strings_continue::class)
    class decode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Buffer>()) }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<Client.Strings_continue>().id } }
    }

    @DependsOn(Buffer::class, Client.Strings_continue::class)
    class decodeActive : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Buffer>()) }
                .and { it.instructions.none { it.opcode == GETSTATIC && it.fieldId == field<Client.Strings_continue>().id } }
    }

    @MethodParameters("indexA", "indexB")
    class swapItems : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
    }

    @DependsOn(swapItems::class)
    class itemIds : OrderMapper.InMethod.Field(swapItems::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(swapItems::class)
    class itemQuantities : OrderMapper.InMethod.Field(swapItems::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(decode::class)
    class type : OrderMapper.InMethod.Field(decode::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters("buffer")
    @DependsOn(Buffer::class)
    class readInts : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == IntArray::class.type }
                .and { it.arguments.startsWith(type<Buffer>()) }
    }

    @MethodParameters("buffer")
    @DependsOn(Buffer::class)
    class readParameters : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Array<Any?>::class.type }
                .and { it.arguments.startsWith(type<Buffer>()) }
    }

    @MethodParameters("b")
    @DependsOn(SpriteMask::class)
    class getSpriteMask : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<SpriteMask>() }
    }

    class inventoryXOffsets : WidgetInvArray(0)
    class inventoryYOffsets : WidgetInvArray(1)
    class inventorySprites : WidgetInvArray(2)


    @DependsOn(Sprite::class)
    class getInventorySprite : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Sprite>() }
                .and { it.arguments == listOf(INT_TYPE) }
    }

    @MethodParameters("b")
    @DependsOn(Sprite::class)
    class getSprite : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Sprite>() }
                .and { it.arguments == listOf(BOOLEAN_TYPE) }
    }

    // always null?
    class ints0 : Widget10Array(0)
    class ints1 : Widget10Array(1)

    class rectangleMode : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type.arrayDimensions == 0 }
                .and { it.type != it.klass.type }
                .and { it.type in it.jar }
    }
}