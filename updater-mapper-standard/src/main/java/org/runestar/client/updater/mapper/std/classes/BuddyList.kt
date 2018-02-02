package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.*
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import java.lang.reflect.Modifier

@SinceVersion(162)
@DependsOn(Buddy::class)
class BuddyList : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { it.type == type<Buddy>().withDimensions(1) } }

    @MethodParameters()
    class newInstance : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { Modifier.isAbstract(it.access) }
                .and { it.returnType == type<Buddy>() }
    }

    @MethodParameters("size")
    class newTypedArray : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { Modifier.isAbstract(it.access) }
                .and { it.returnType == type<Buddy>().withDimensions(1) }
    }

    class array : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Buddy>().withDimensions(1) }
    }

    @MethodParameters()
    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.any { it.isMethod && it.methodId == HashMap<*, *>::clear.id } }
    }

    class size0 : OrderMapper.InConstructor.Field(BuddyList::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class capacity : OrderMapper.InConstructor.Field(BuddyList::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters("index")
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Buddy>() }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == ArrayIndexOutOfBoundsException::class.type } }
    }

    @MethodParameters()
    class sort : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE && it.instructions.any { it.isMethod && it.methodName == "sort" } }
    }

    class usernamesMap : OrderMapper.InConstructor.Field(BuddyList::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == HashMap::class.type }
    }

    class previousUsernamesMap : OrderMapper.InConstructor.Field(BuddyList::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == HashMap::class.type }
    }

    @MethodParameters("buddy")
    class mapRemove : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Buddy>()) }
                .and { it.instructions.any { it.isMethod && it.methodName == "remove" } }
    }

    @MethodParameters("buddy")
    class mapPut : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Buddy>()) }
                .and { it.instructions.any { it.isMethod && it.methodName == "put" } }
    }

    @MethodParameters("username")
    class contains : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Username>()) }
                .and { it.instructions.any { it.isMethod && it.methodName == "containsKey" } }
    }

    @MethodParameters()
    @DependsOn(capacity::class)
    class isFull : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.isField && it.fieldId == field<capacity>().id } }
    }

    @MethodParameters()
    class size : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.none { it.opcode == IINC } }
    }

    @MethodParameters("buddy", "username", "previousUsername")
    class changeName : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 3..4 }
                .and { it.arguments.startsWith(type<Buddy>(), type<Username>(), type<Username>()) }
    }

    @MethodParameters("buddy")
    class indexOf : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Buddy>()) }
                .and { it.instructions.any { it.opcode == ICONST_M1 } }
    }

    @MethodParameters("username")
    @DependsOn(usernamesMap::class)
    class getByCurrentUsername : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Buddy>() }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Username>()) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<usernamesMap>().id } }
    }

    @MethodParameters("previousUsername")
    @DependsOn(previousUsernamesMap::class)
    class getByPreviousUsername : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Buddy>() }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Username>()) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<previousUsernamesMap>().id } }
    }

    @MethodParameters("username")
    @DependsOn(getByPreviousUsername::class)
    class getByUsername : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Buddy>() }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Username>()) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<getByPreviousUsername>().id } }
    }

    @MethodParameters("buddy")
    class arrayAddLast : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Buddy>()) }
                .and { it.instructions.none { it.isMethod } }
    }

    @MethodParameters("username", "previousUsername")
    class addLast : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Buddy>() }
                .and { it.arguments.size in 2..3 }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == IllegalStateException::class.type } }
    }

    @MethodParameters("username")
    @DependsOn(addLast::class)
    class addLastCurrentUsernameOnly : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Buddy>() }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<addLast>().id } }
    }

    @MethodParameters("index")
    class arrayRemove : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.isMethod && it.methodName == "arraycopy" } }
    }

    @MethodParameters("buddy")
    @DependsOn(arrayRemove::class)
    class remove : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<arrayRemove>().id } }
    }

    @MethodParameters("username")
    @DependsOn(remove::class)
    class removeByUsername : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<remove>().id } }
    }
}