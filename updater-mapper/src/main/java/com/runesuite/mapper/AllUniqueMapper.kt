package com.runesuite.mapper

import com.runesuite.mapper.tree.*
import com.runesuite.mapper.extensions.Predicate
import java.lang.reflect.Modifier

abstract class AllUniqueMapper<T>() : Mapper<T>(), InstructionResolver<T> {

    override fun match(jar: Jar2): T {
        val matches = jar.classes.asSequence()
                .flatMap { it.methods.asSequence() }
                .flatMap { it.instructions }
                .filter(predicate)
                .map { resolve(it) }
                .toSet()
        check(matches.isNotEmpty()) { "$this: No matches" }
        check(matches.size == 1) { "$this: Multiple matches: $matches" }
        return matches.first()
    }

    abstract val predicate: Predicate<Instruction2>

    abstract class Class : AllUniqueMapper<Class2>(), ElementMatcher.Class, InstructionResolver.Class

    abstract class Field : AllUniqueMapper<Field2>(), ElementMatcher.Field, InstructionResolver.Field

    abstract class Method : AllUniqueMapper<Method2>(), ElementMatcher.Method, InstructionResolver.Method
}