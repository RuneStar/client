package org.runestar.client.injector

import net.bytebuddy.description.field.FieldDescription
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.jar.asm.Type
import net.bytebuddy.matcher.ElementMatcher
import org.runestar.client.updater.HOOKS
import org.runestar.client.updater.common.FieldHook
import org.runestar.client.updater.common.MethodHook

internal val FieldHook.getterName get() = "get${field.capitalize()}"

internal val FieldHook.setterName get() = "set${field.capitalize()}"

internal fun FieldHook.matcher(): ElementMatcher<FieldDescription> = ElementMatcher { it.name == name }

internal fun FieldHook.getterMatcher(): ElementMatcher<MethodDescription> = ElementMatcher { it.name == getterName }

internal fun FieldHook.setterMatcher(): ElementMatcher<MethodDescription> = ElementMatcher { it.name == setterName }

internal val MethodHook.argumentsCount get() = Type.getMethodType(descriptor).argumentTypes.size

internal val MethodHook.actualArgumentsCount get() = parameters!!.size

internal fun MethodHook.matcher(): ElementMatcher<MethodDescription> = ElementMatcher { it.name == name && it.descriptor == descriptor }

internal fun MethodHook.proxyMatcher(): ElementMatcher<MethodDescription> = ElementMatcher { it.name == method }

internal fun hookClassNames(): Collection<String> {
    return HOOKS.flatMap { (it.methods.map { it.owner }).plus(it.name) }.toSet()
}