package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.lang.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.GOTO
import org.objectweb.asm.Type.VOID_TYPE
import java.lang.reflect.Modifier

@DependsOn(Node::class)
class NodeDeque : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.size == 2 }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.count { it.type == type<Node>() } == 2 }

    @DependsOn(Node::class)
    class sentinel : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Node>() }
                .and { Modifier.isPublic(it.access) }
    }

    @DependsOn(Node::class, sentinel::class)
    class current : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Node>() }
                .and { it.id != field<sentinel>().id }
    }

    @MethodParameters("node")
    @DependsOn(Node::class, Node.next::class)
    class addFirst : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(type<Node>()) }
                .and { it.returnType == VOID_TYPE }
                .and { it.instructions.filter { it.isField && it.fieldId == field<Node.next>().id }.count() == 5 }
    }

    @MethodParameters("node")
    @DependsOn(Node::class, Node.next::class)
    class addLast : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(type<Node>()) }
                .and { it.returnType == VOID_TYPE }
                .and { it.instructions.filter { it.isField && it.fieldId == field<Node.next>().id }.count() == 4 }
    }

    @MethodParameters
    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 0..1 }
                .and { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == GOTO } }
    }

    @MethodParameters
    @DependsOn(Node::class, Node.remove::class, Node.next::class)
    class next : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 0..1 }
                .and { it.returnType == type<Node>() }
                .and { it.instructions.none { it.isMethod && it.methodId == method<Node.remove>().id } }
                .and { it.instructions.count { it.isField && it.fieldId == field<Node.next>().id } == 1 }
    }

    @MethodParameters
    @DependsOn(Node::class, Node.remove::class, Node.previous::class)
    class previous : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 0..1 }
                .and { it.returnType == type<Node>() }
                .and { it.instructions.none { it.isMethod && it.methodId == method<Node.remove>().id } }
                .and { it.instructions.count { it.isField && it.fieldId == field<Node.previous>().id } == 1 }
    }

    @MethodParameters
    @DependsOn(Node::class, Node.remove::class, Node.next::class)
    class first : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 0..1 }
                .and { it.returnType == type<Node>() }
                .and { it.instructions.none { it.isMethod && it.methodId == method<Node.remove>().id } }
                .and { it.instructions.count { it.isField && it.fieldId == field<Node.next>().id } == 2 }
    }

    @MethodParameters
    @DependsOn(Node::class, Node.remove::class, Node.previous::class)
    class last : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 0..1 }
                .and { it.returnType == type<Node>() }
                .and { it.instructions.none { it.isMethod && it.methodId == method<Node.remove>().id } }
                .and { it.instructions.count { it.isField && it.fieldId == field<Node.previous>().id } == 2 }
    }

    @MethodParameters
    @DependsOn(Node::class, Node.remove::class, Node.next::class)
    class removeFirst : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 0..1 }
                .and { it.returnType == type<Node>() }
                .and { it.instructions.any { it.isMethod && it.methodId == method<Node.remove>().id } }
                .and { it.instructions.count { it.isField && it.fieldId == field<Node.next>().id } == 1 }
    }

    @MethodParameters
    @DependsOn(Node::class, Node.remove::class, Node.previous::class)
    class removeLast : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 0..1 }
                .and { it.returnType == type<Node>() }
                .and { it.instructions.any { it.isMethod && it.methodId == method<Node.remove>().id } }
                .and { it.instructions.count { it.isField && it.fieldId == field<Node.previous>().id } == 1 }
    }
}