package com.runesuite.client.updater.map

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Message::class)
class ChatBox : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { it.type == type<Message>().withDimensions(1) } }

    @MethodParameters("type", "sender", "text", "prefix")
    @DependsOn(Message::class)
    class addMessage : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Message>() }
                .and { it.arguments.startsWith(INT_TYPE, String::class.type, String::class.type, String::class.type) }
    }

    @DependsOn(Message::class)
    class messages : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Message>().withDimensions(1) }
    }

    class int1 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }
}