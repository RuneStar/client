package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Type.*
import java.lang.reflect.Modifier

class TextureLoader : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { Modifier.isInterface(it.access) }
            .and { it.instanceMethods.size == 4 }
            .and { it.instanceMethods.count { it.returnType == BOOLEAN_TYPE } == 2 }

//    @MethodParameters()
    class load : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == IntArray::class.type }
    }
}