package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@SinceVersion(141)
@DependsOn(BoundingBox::class, Model::class)
class BoundingBox3D : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<BoundingBox>() }
            .and { it.constructors.first().arguments[0] == type<Model>() }

    @MethodParameters()
    @DependsOn(BoundingBox.draw::class)
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<BoundingBox.draw>().mark }
    }

    class int1 : OrderMapper.InConstructor.Field(BoundingBox3D::class, 0, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int2 : OrderMapper.InConstructor.Field(BoundingBox3D::class, 1, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int3 : OrderMapper.InConstructor.Field(BoundingBox3D::class, 2, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int4 : OrderMapper.InConstructor.Field(BoundingBox3D::class, 3, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int5 : OrderMapper.InConstructor.Field(BoundingBox3D::class, 4, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class int6 : OrderMapper.InConstructor.Field(BoundingBox3D::class, 5, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class color : OrderMapper.InConstructor.Field(BoundingBox3D::class, 6, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}