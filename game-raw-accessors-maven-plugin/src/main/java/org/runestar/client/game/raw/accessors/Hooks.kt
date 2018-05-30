package org.runestar.client.game.raw.accessors

import org.objectweb.asm.Type
import org.runestar.client.updater.common.ClassHook
import org.runestar.client.updater.common.MethodHook

internal val MethodHook.type get() = Type.getMethodType(descriptor)

internal val MethodHook.actualParameters: List<ParameterHook>
    get() = type.argumentTypes.take(parameters!!.size).mapIndexed { i, type -> ParameterHook(parameters!![i], type.descriptor) }

internal data class ParameterHook(val name: String, val descriptor: String)

internal val ClassHook.descriptor get() = Type.getObjectType(name).descriptor