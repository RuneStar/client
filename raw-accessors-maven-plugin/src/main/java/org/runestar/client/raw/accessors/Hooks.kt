package org.runestar.client.raw.accessors

import org.objectweb.asm.Type
import org.runestar.client.updater.common.ClassHook
import org.runestar.client.updater.common.ConstructorHook
import org.runestar.client.updater.common.MethodHook

internal val ClassHook.interfaceDescriptors: List<String> get() = interfaces.map { "L$it;" }

internal val ConstructorHook.argumentDescriptors: List<String> get() = Type.getArgumentTypes(descriptor).map { it.descriptor }

internal val MethodHook.argumentDescriptors: List<String> get() = Type.getArgumentTypes(descriptor).map { it.descriptor }

internal val MethodHook.returnDescriptor: String get() = Type.getReturnType(descriptor).descriptor

internal val MethodHook.actualParameters: List<ParameterHook> get() {
    return argumentDescriptors.take(checkNotNull(parameters).size).mapIndexed { i, desc ->
        ParameterHook(checkNotNull(parameters)[i], desc)
    }
}

internal data class ParameterHook(val name: String, val descriptor: String)