package org.runestar.client.game.inject

import org.runestar.client.updater.common.FieldHook
import org.runestar.client.updater.common.MethodHook
import net.bytebuddy.jar.asm.Type

internal val FieldHook.getterName get() = "get${field.capitalize()}"

internal val FieldHook.setterName get() = "set${field.capitalize()}"

internal val MethodHook.argumentsCount get() = Type.getMethodType(descriptor).argumentTypes.size

internal val MethodHook.actualArgumentsCount get() = parameters!!.size