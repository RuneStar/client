package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.id
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@DependsOn(Node::class)
class PlatformInfo : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.interfaces.isEmpty() }
            .and { it.constructors.first().instructions.any { it.isMethod && it.methodId == Runtime::maxMemory.id } }

    // mac, windows, linux, other
    class osType : OrderMapper.InConstructor.Field(PlatformInfo::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    // is amd64 or x86_64
    class isX64 : OrderMapper.InConstructor.Field(PlatformInfo::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(osType::class)
    class osVersionId : OrderMapper.InConstructor.Field(PlatformInfo::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldId != field<osType>().id }
    }

    @DependsOn(osType::class, osVersionId::class)
    class javaVendorType : OrderMapper.InConstructor.Field(PlatformInfo::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
                .and { it.fieldId != field<osType>().id }
                .and { it.fieldId != field<osVersionId>().id }
    }
}