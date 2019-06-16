package org.runestar.client.updater.mapper

interface ElementMatcher<out T> {
    fun match(jar: Jar2): T

    interface Class : ElementMatcher<Class2>
    interface Field : ElementMatcher<Field2>
    interface Method : ElementMatcher<Method2>
}