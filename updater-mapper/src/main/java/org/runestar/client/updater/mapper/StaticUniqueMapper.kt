package org.runestar.client.updater.mapper

import org.runestar.client.updater.mapper.tree.*
import org.runestar.client.updater.mapper.extensions.Predicate
import java.lang.reflect.Modifier

abstract class StaticUniqueMapper<T>() : Mapper<T>(), InstructionResolver<T> {

    override fun match(jar: Jar2): T {
        val matches = jar.classes.asSequence()
                .flatMap { it.methods.asSequence() }
                .filter { Modifier.isStatic(it.access) }
                .flatMap { it.instructions }
                .filter(predicate)
                .map { resolve(it) }
                .toSet()
        check(matches.isNotEmpty()) { "$this: No matches" }
        check(matches.size == 1) { "$this: Multiple matches: $matches" }
        return matches.first()
    }

    abstract val predicate: Predicate<Instruction2>

    abstract class Class : StaticUniqueMapper<Class2>(), ElementMatcher.Class, InstructionResolver.Class

    abstract class Field : StaticUniqueMapper<Field2>(), ElementMatcher.Field, InstructionResolver.Field

    abstract class Method : StaticUniqueMapper<Method2>(), ElementMatcher.Method, InstructionResolver.Method
}