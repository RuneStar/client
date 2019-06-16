package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import java.lang.reflect.Modifier

@DependsOn(DualNode::class)
class Entity : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.size == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 1 }
            .and { Modifier.isAbstract(it.access) }
            .and { it.instanceMethods.none { Modifier.isAbstract(it.access) } }

    class height : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    @MethodParameters()
    class getModel : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType in it.jar }
    }

    @MethodParameters("yaw", "cameraPitchSine", "cameraPitchCosine", "cameraYawSine", "cameraYawCosine", "x", "y", "z", "tag")
    class draw : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
    }
}