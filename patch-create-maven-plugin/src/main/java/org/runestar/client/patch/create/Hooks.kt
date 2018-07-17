package org.runestar.client.patch.create

import net.bytebuddy.description.field.FieldDescription
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.matcher.ElementMatcher
import org.runestar.client.updater.common.ClassHook
import org.runestar.client.updater.common.FieldHook
import org.runestar.client.updater.common.MethodHook

internal fun FieldHook.matcher(): ElementMatcher<FieldDescription> = ElementMatcher { it.name == name }

internal fun FieldHook.getterMatcher(): ElementMatcher<MethodDescription> = ElementMatcher { it.name == getterMethod }

internal fun FieldHook.setterMatcher(): ElementMatcher<MethodDescription> = ElementMatcher { it.name == setterMethod }

internal fun MethodHook.matcher(): ElementMatcher<MethodDescription> = ElementMatcher { it.name == name && it.descriptor == descriptor }

internal fun MethodHook.proxyMatcher(): ElementMatcher<MethodDescription> = ElementMatcher { it.name == method }

internal fun hookClassNames(hooks: List<ClassHook>): Collection<String> {
    return hooks.flatMap { (it.methods.map { it.owner }).plus(it.name) }.toSet()
}