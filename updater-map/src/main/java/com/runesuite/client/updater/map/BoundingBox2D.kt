package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE

@SinceVersion(141)
@DependsOn(BoundingBox::class)
class BoundingBox2D : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<BoundingBox>() }
            .and { it.constructors.first().arguments[0] == INT_TYPE }

    @MethodParameters()
    @DependsOn(BoundingBox.draw::class)
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<BoundingBox.draw>().mark }
    }

    class xMin : OrderMapper.InConstructor.Field(BoundingBox2D::class, 0, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class yMin : OrderMapper.InConstructor.Field(BoundingBox2D::class, 1, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class xMax : OrderMapper.InConstructor.Field(BoundingBox2D::class, 2, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class yMax : OrderMapper.InConstructor.Field(BoundingBox2D::class, 3, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class color : OrderMapper.InConstructor.Field(BoundingBox2D::class, 4, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}