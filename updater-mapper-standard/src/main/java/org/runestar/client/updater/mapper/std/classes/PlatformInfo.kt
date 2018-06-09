package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.BIPUSH
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.id
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.std.PlatformInfoString
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Node::class)
class PlatformInfo : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.interfaces.isEmpty() }
            .and { it.constructors.isNotEmpty() }
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

    @MethodParameters("buffer")
    @DependsOn(Buffer::class)
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Buffer>()) }
    }

    @MethodParameters()
    class limitStringLengths : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 40 } }
    }

    @MethodParameters()
    class length : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
    }

    class string0 : PlatformInfoString(0)
    class string1 : PlatformInfoString(1)
    class string2 : PlatformInfoString(2)
    class string3 : PlatformInfoString(3)
    class string4 : PlatformInfoString(4)
    class string5 : PlatformInfoString(5)
}