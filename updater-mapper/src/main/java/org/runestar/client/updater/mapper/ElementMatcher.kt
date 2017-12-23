package org.runestar.client.updater.mapper

import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Jar2
import org.runestar.client.updater.mapper.tree.Method2

interface ElementMatcher<out T> {
    fun match(jar: Jar2): T

    interface Class : ElementMatcher<Class2>
    interface Field : ElementMatcher<Field2>
    interface Method : ElementMatcher<Method2>
}