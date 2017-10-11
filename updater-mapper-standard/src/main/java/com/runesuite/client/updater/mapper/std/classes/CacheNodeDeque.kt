package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.GOTO
import org.objectweb.asm.Type.VOID_TYPE

@DependsOn(CacheNode::class)
class CacheNodeDeque : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.size == 1 }
            .and { it.instanceFields.all { it.type == type<CacheNode>() } }

    class sentinel : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { true }
    }

    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == GOTO } }
    }

    @DependsOn(CacheNode::class, CacheNode.cachePrevious::class)
    class last : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<CacheNode>() }
                .and { it.instructions.filter { it.isField && it.fieldId == field<CacheNode.cachePrevious>().id }.count() == 1 }
                .and { it.instructions.none { it.isMethod } }
    }

    @DependsOn(CacheNode::class, CacheNode.cachePrevious::class)
    class removeLast : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<CacheNode>() }
                .and { it.instructions.filter { it.isField && it.fieldId == field<CacheNode.cachePrevious>().id }.count() == 1 }
                .and { it.instructions.any { it.isMethod } }
    }

    @DependsOn(CacheNode.cachePrevious::class)
    class addFirst : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.filter { it.isField && it.fieldId == field<CacheNode.cachePrevious>().id }.count() == 3 }
    }

    @DependsOn(CacheNode.cachePrevious::class)
    class addLast : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.filter { it.isField && it.fieldId == field<CacheNode.cachePrevious>().id }.count() == 4 }
    }
}