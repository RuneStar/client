package com.runesuite.mapper

import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Jar2
import com.runesuite.mapper.tree.Method2

interface ElementMatcher<out T> {
    fun match(jar: Jar2): T

    interface Class : ElementMatcher<Class2>
    interface Field : ElementMatcher<Field2>
    interface Method : ElementMatcher<Method2>
}