package org.runestar.client.patch

import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.scaffold.InstrumentedType
import net.bytebuddy.implementation.Implementation
import net.bytebuddy.implementation.MethodCall
import net.bytebuddy.implementation.bytecode.constant.DefaultValue

internal fun MethodCall.withAllArgumentsThenDefaultValues(): MethodCall {
    return with(object : MethodCall.ArgumentLoader.Factory {

        override fun prepare(instrumentedType: InstrumentedType): InstrumentedType = instrumentedType

        override fun make(
                implementationTarget: Implementation.Target,
                instrumentedType: TypeDescription,
                instrumentedMethod: MethodDescription,
                invokedMethod: MethodDescription
        ): MutableList<MethodCall.ArgumentLoader> {
            val argumentLoaders = ArrayList<MethodCall.ArgumentLoader>(invokedMethod.parameters.size)
            for (parameterDescription in instrumentedMethod.parameters) {
                argumentLoaders.add(MethodCall.ArgumentLoader.ForMethodParameter(parameterDescription.index, instrumentedMethod))
            }
            for (parameterDescription in invokedMethod.parameters.drop(argumentLoaders.size)) {
                argumentLoaders.add(MethodCall.ArgumentLoader.ForStackManipulation(DefaultValue.of(parameterDescription.type), parameterDescription.type))
            }
            return argumentLoaders
        }
    })
}