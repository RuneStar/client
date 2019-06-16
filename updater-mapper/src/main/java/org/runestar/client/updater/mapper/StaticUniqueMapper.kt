package org.runestar.client.updater.mapper

import java.lang.reflect.Modifier

abstract class StaticUniqueMapper<T>() : Mapper<T>(), InstructionResolver<T> {

    override fun match(jar: Jar2): T {
        return jar.classes.asSequence()
                .flatMap { it.methods.asSequence() }
                .filter { Modifier.isStatic(it.access) }
                .flatMap { it.instructions }
                .filter(predicate)
                .mapTo(HashSet()) { resolve(it) }
                .single()
    }

    abstract val predicate: Predicate<Instruction2>

    abstract class Class : StaticUniqueMapper<Class2>(), ElementMatcher.Class, InstructionResolver.Class

    abstract class Field : StaticUniqueMapper<Field2>(), ElementMatcher.Field, InstructionResolver.Field

    abstract class Method : StaticUniqueMapper<Method2>(), ElementMatcher.Method, InstructionResolver.Method
}