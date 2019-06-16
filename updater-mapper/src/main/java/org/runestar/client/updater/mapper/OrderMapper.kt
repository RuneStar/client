package org.runestar.client.updater.mapper

import kotlin.reflect.KClass

abstract class OrderMapper<T>(val position: Int, val maxMatches: Int = Int.MAX_VALUE) : Mapper<T>(), InstructionResolver<T> {

    abstract val method: Method2

    override fun match(jar: Jar2): T {
        val n = position.takeUnless { it < 0 } ?: position * -1 - 1
        val insns = if (position >= 0) method.instructions else method.instructions.toList().asReversed().asSequence()
        val matches = insns.filter(predicate).toList()
        check(matches.size <= maxMatches)
        return resolve(matches[n])
    }

    abstract val predicate: Predicate<Instruction2>

    abstract class InMethod<T>(val methodMapper: KClass<out Mapper<Method2>>, position: Int, maxMatches: Int = Int.MAX_VALUE) : OrderMapper<T>(position, maxMatches) {

        override val method get() = context.methods.getValue(methodMapper)

        abstract class Class(
                methodMapper: KClass<out Mapper<Method2>>, position: Int, maxMatches: Int = Int.MAX_VALUE
        ) : InMethod<Class2>(methodMapper, position, maxMatches), ElementMatcher.Class, InstructionResolver.Class

        abstract class Field(
                methodMapper: KClass<out Mapper<Method2>>, position: Int, maxMatches: Int = Int.MAX_VALUE
        ) : InMethod<Field2>(methodMapper, position, maxMatches), ElementMatcher.Field, InstructionResolver.Field

        abstract class Method(
                methodMapper: KClass<out Mapper<Method2>>, position: Int, maxMatches: Int = Int.MAX_VALUE
        ) : InMethod<Method2>(methodMapper, position, maxMatches), ElementMatcher.Method, InstructionResolver.Method
    }

    abstract class InConstructor<T>(val classMapper: KClass<out Mapper<Class2>>, position: Int, maxMatches: Int = Int.MAX_VALUE) : OrderMapper<T>(position, maxMatches) {

        override val method: Method2 get() {
            return context.classes.getValue(classMapper).constructors.single(constructorPredicate)
        }

        open val constructorPredicate = predicateOf<Method2> { true }

        abstract class Class(
                classMapper: KClass<out Mapper<Class2>>, position: Int, maxMatches: Int = Int.MAX_VALUE
        ) : InConstructor<Class2>(classMapper, position, maxMatches), ElementMatcher.Class, InstructionResolver.Class

        abstract class Field(
                classMapper: KClass<out Mapper<Class2>>, position: Int, maxMatches: Int = Int.MAX_VALUE
        ) : InConstructor<Field2>(classMapper, position, maxMatches), ElementMatcher.Field, InstructionResolver.Field

        abstract class Method(
                classMapper: KClass<out Mapper<Class2>>, position: Int, maxMatches: Int = Int.MAX_VALUE
        ) : InConstructor<Method2>(classMapper, position, maxMatches), ElementMatcher.Method, InstructionResolver.Method
    }

    abstract class InClassInitializer<T>(val classMapper: KClass<out Mapper<Class2>>, position: Int, maxMatches: Int = Int.MAX_VALUE) : OrderMapper<T>(position, maxMatches) {

        override val method: Method2 get() {
            return context.classes.getValue(classMapper).classInitializer!!
        }

        abstract class Class(
                classMapper: KClass<out Mapper<Class2>>, position: Int, maxMatches: Int = Int.MAX_VALUE
        ) : InClassInitializer<Class2>(classMapper, position, maxMatches), ElementMatcher.Class, InstructionResolver.Class

        abstract class Field(
                classMapper: KClass<out Mapper<Class2>>, position: Int, maxMatches: Int = Int.MAX_VALUE
        ) : InClassInitializer<Field2>(classMapper, position, maxMatches), ElementMatcher.Field, InstructionResolver.Field

        abstract class Method(
                classMapper: KClass<out Mapper<Class2>>, position: Int, maxMatches: Int = Int.MAX_VALUE
        ) : InClassInitializer<Method2>(classMapper, position, maxMatches), ElementMatcher.Method, InstructionResolver.Method
    }
}