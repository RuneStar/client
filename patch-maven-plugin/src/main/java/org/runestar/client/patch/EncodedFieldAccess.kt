package org.runestar.client.patch

import net.bytebuddy.description.field.FieldDescription
import net.bytebuddy.implementation.FieldAccessor
import net.bytebuddy.implementation.Implementation
import net.bytebuddy.implementation.bytecode.Multiplication
import net.bytebuddy.implementation.bytecode.StackManipulation
import net.bytebuddy.implementation.bytecode.assign.Assigner
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant
import net.bytebuddy.implementation.bytecode.constant.LongConstant
import net.bytebuddy.implementation.bytecode.member.FieldAccess
import net.bytebuddy.implementation.bytecode.member.MethodReturn
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess
import org.runestar.client.updater.common.invert

interface EncodedFieldAccess {

    val getter: Implementation

    val setter: Implementation

    data class ForInt(val field: FieldDescription.InDefinedShape, val decoder: Int) : EncodedFieldAccess {

        override val getter: Implementation get() = Implementation.Simple(
                if (this.field.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                FieldAccess.forField(this.field).read(),
                IntegerConstant.forValue(decoder),
                Multiplication.INTEGER,
                MethodReturn.INTEGER
        )

        override val setter: Implementation get() = Implementation.Simple(
                if (this.field.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                IntegerConstant.forValue(invert(decoder)),
                MethodVariableAccess.INTEGER.loadFrom(1),
                Multiplication.INTEGER,
                FieldAccess.forField(this.field).write(),
                MethodReturn.VOID
        )
    }

    data class ForLong(val field: FieldDescription.InDefinedShape, val decoder: Long) : EncodedFieldAccess {

        override val getter: Implementation get() = Implementation.Simple(
                if (this.field.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                FieldAccess.forField(this.field).read(),
                LongConstant.forValue(decoder),
                Multiplication.LONG,
                MethodReturn.LONG
        )

        override val setter: Implementation get() = Implementation.Simple(
                if (this.field.isStatic) StackManipulation.Trivial.INSTANCE else MethodVariableAccess.loadThis(),
                LongConstant.forValue(invert(decoder)),
                MethodVariableAccess.LONG.loadFrom(1),
                Multiplication.LONG,
                FieldAccess.forField(this.field).write(),
                MethodReturn.VOID
        )
    }

    data class None(val field: FieldDescription.InDefinedShape) : EncodedFieldAccess {

        private val either get() = FieldAccessor.ofField(field.name).`in`(field.declaringType).withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC)

        override val getter: Implementation get() = either

        override val setter: Implementation get() = either
    }

    companion object {

        fun of(field: FieldDescription.InDefinedShape, decoder: Number?): EncodedFieldAccess = when (decoder) {
            null -> None(field)
            is Int -> ForInt(field, decoder)
            is Long -> ForLong(field, decoder)
            else -> error(decoder)
        }
    }
}