package org.runestar.client.updater.mapper

import org.runestar.client.updater.mapper.tree.*
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.predicateOf
import kotlin.reflect.KClass

/**
 * Matches one or more instructions in a method that all resolve to the same element.
 */
abstract class UniqueMapper<T> : Mapper<T>(), InstructionResolver<T> {

    abstract val method: Method2

    override fun match(jar: Jar2): T {
        val matches = method.instructions.filter(predicate).map { resolve(it) }.toSet()
        check(matches.isNotEmpty()) { "$this: No matches" }
        check(matches.size == 1) { "$this: Multiple matches: $matches" }
        return matches.first()
    }

    abstract val predicate: Predicate<Instruction2>

    abstract class InMethod<T>(val methodMapper: KClass<out Mapper<Method2>>) : UniqueMapper<T>() {

        override val method get() = context.methods.getValue(methodMapper)

        abstract class Class(
                methodMapper: KClass<out Mapper<Method2>>
        ) : InMethod<Class2>(methodMapper), ElementMatcher.Class, InstructionResolver.Class

        abstract class Field(
                methodMapper: KClass<out Mapper<Method2>>
        ) : InMethod<Field2>(methodMapper), ElementMatcher.Field, InstructionResolver.Field

        abstract class Method(
                methodMapper: KClass<out Mapper<Method2>>
        ) : InMethod<Method2>(methodMapper), ElementMatcher.Method, InstructionResolver.Method
    }

    abstract class InConstructor<T>(val classMapper: KClass<out Mapper<Class2>>) : UniqueMapper<T>() {

        override val method: Method2 get() {
            val matches = context.classes.getValue(classMapper).constructors.filter(constructorPredicate).toList()
            check(matches.isNotEmpty()) { "$this: No matcher" }
            check(matches.size == 1) { "$this: multiple matches: $matches" }
            return matches.first()
        }

        open val constructorPredicate = predicateOf<Method2> { true }

        abstract class Class(
                classMapper: KClass<out Mapper<Class2>>
        ) : InConstructor<Class2>(classMapper), ElementMatcher.Class, InstructionResolver.Class

        abstract class Field(
                classMapper: KClass<out Mapper<Class2>>
        ) : InConstructor<Field2>(classMapper), ElementMatcher.Field, InstructionResolver.Field

        abstract class Method(
                classMapper: KClass<out Mapper<Class2>>
        ) : InConstructor<Method2>(classMapper), ElementMatcher.Method, InstructionResolver.Method
    }

    abstract class InClassInitializer<T>(val classMapper: KClass<out Mapper<Class2>>) : UniqueMapper<T>() {

        override val method: Method2 get() {
            return context.classes.getValue(classMapper).classInitializer!!
        }

        abstract class Class(
                classMapper: KClass<out Mapper<Class2>>
        ) : InClassInitializer<Class2>(classMapper), ElementMatcher.Class, InstructionResolver.Class

        abstract class Field(
                classMapper: KClass<out Mapper<Class2>>
        ) : InClassInitializer<Field2>(classMapper), ElementMatcher.Field, InstructionResolver.Field

        abstract class Method(
                classMapper: KClass<out Mapper<Class2>>
        ) : InClassInitializer<Method2>(classMapper), ElementMatcher.Method, InstructionResolver.Method
    }
}