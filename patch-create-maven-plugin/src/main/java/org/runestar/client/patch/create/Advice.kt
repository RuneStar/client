package org.runestar.client.patch.create

import net.bytebuddy.asm.Advice
import net.bytebuddy.description.field.FieldDescription
import net.bytebuddy.implementation.bytecode.assign.Assigner
import java.lang.reflect.Field

internal fun <T : Annotation> Advice.WithCustomMapping.bind(
        type: Class<T>,
        field: Field,
        assigner: Assigner.Typing
): Advice.WithCustomMapping {
    return bind(
            Advice.OffsetMapping.ForField.Resolved.Factory(
                    type,
                    FieldDescription.ForLoadedField(field),
                    true,
                    assigner
            )
    )
}