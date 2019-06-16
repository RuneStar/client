package org.runestar.client.updater.mapper

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import org.objectweb.asm.Type
import kotlin.reflect.KClass

abstract class Mapper<T> : ElementMatcher<T> {

    lateinit var context: Context

    inline fun <reified T: Mapper<Class2>> klass(): Class2 {
        return context.classes.getValue(T::class)
    }

    inline fun <reified T: Mapper<Class2>> type(): Type {
        return klass<T>().type
    }

    inline fun <reified T: Mapper<Field2>> field(): Field2 {
        return context.fields.getValue(T::class)
    }

    inline fun <reified T: Mapper<Method2>> method(): Method2 {
        return context.methods.getValue(T::class)
    }

    fun map(jar: Jar2) {
        val t = match(jar)
        val klass = this::class
        @Suppress("UNCHECKED_CAST")
        when (this) {
            is ElementMatcher.Class -> {
                klass as KClass<out Mapper<Class2>>
                t as Class2
                check(!context.classes.inverse().containsKey(t))
                context.classes[klass] = t
            }
            is ElementMatcher.Field -> {
                klass as KClass<out Mapper<Field2>>
                t as Field2
                check(!context.fields.inverse().containsKey(t))
                context.fields[klass] = t
            }
            is ElementMatcher.Method -> {
                klass as KClass<out Mapper<Method2>>
                t as Method2
                check(!context.methods.inverse().containsKey(t))
                context.methods[klass] = t
            }
            else -> error(this)
        }
    }

    class Context {

        val classes: BiMap<KClass<out Mapper<Class2>>, Class2> = HashBiMap.create()

        val fields: BiMap<KClass<out Mapper<Field2>>, Field2> = HashBiMap.create()

        val methods: BiMap<KClass<out Mapper<Method2>>, Method2> = HashBiMap.create()
    }
}

