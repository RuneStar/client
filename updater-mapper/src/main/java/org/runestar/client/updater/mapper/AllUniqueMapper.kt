package org.runestar.client.updater.mapper

abstract class AllUniqueMapper<T> : Mapper<T>(), InstructionResolver<T> {

    override fun match(jar: Jar2): T {
        return jar.classes.asSequence()
                .flatMap { it.methods.asSequence() }
                .flatMap { it.instructions }
                .filter(predicate)
                .mapTo(HashSet()) { resolve(it) }
                .single()
    }

    abstract val predicate: Predicate<Instruction2>

    abstract class Class : AllUniqueMapper<Class2>(), ElementMatcher.Class, InstructionResolver.Class

    abstract class Field : AllUniqueMapper<Field2>(), ElementMatcher.Field, InstructionResolver.Field

    abstract class Method : AllUniqueMapper<Method2>(), ElementMatcher.Method, InstructionResolver.Method
}