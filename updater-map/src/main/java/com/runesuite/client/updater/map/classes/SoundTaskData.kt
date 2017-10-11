package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Type.*
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.SourceDataLine

class SoundTaskData : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.isEmpty() }
            .and { it.instanceFields.any { it.type == SourceDataLine::class.type } }

    class audioFormat : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == AudioFormat::class.type }
    }

    class sourceDataLine : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == SourceDataLine::class.type }
    }

    @MethodParameters()
    class flush : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodName == SourceDataLine::flush.name } }
    }

    @MethodParameters()
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodName == SourceDataLine::close.name } }
    }

    @MethodParameters()
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodName == SourceDataLine::write.name } }
    }

    // remaining ?
    @MethodParameters()
    class available : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodName == SourceDataLine::available.name } }
    }

    @MethodParameters("bufferSize")
    class open : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodName == SourceDataLine::start.name } }
    }
}