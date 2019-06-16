package org.runestar.client.updater.mapper

import com.google.common.reflect.ClassPath
import java.nio.file.Path
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf

class JarMapper(vararg val classMappers: KClass<out Mapper<Class2>>) {

    @Suppress("UNCHECKED_CAST")
    constructor(pkg: String, classLoader: ClassLoader) : this(
            *ClassPath.from(classLoader)
                    .getTopLevelClassesRecursive(pkg)
                    .mapNotNull { it.load().kotlin }
                    .filter { it.isSubclassOf(Mapper::class) && it.isSubclassOf(ElementMatcher.Class::class) }
                    .map { it as KClass<out Mapper<Class2>> }
                    .toTypedArray()
    )

    fun map(jar: Path, context: Mapper.Context) {
        val jar2 = Jar2(jar)
        @Suppress("UNCHECKED_CAST")
        val unordered = classMappers.asSequence()
                .flatMap { it.nestedClasses.asSequence().plus(it) }
                .filter { it.isSubclassOf(Mapper::class) }
                .map { it as KClass<out Mapper<*>> }
        orderDependencies(unordered).map { it.createInstance() }.forEach {
            it.context = context
            it.map(jar2)
        }
    }

    private fun orderDependencies(unordered: Sequence<KClass<out Mapper<*>>>): Sequence<KClass<out Mapper<*>>> {
        val ordered = LinkedHashSet<KClass<out Mapper<*>>>()
        val unorderedCollection = unordered.toHashSet()
        while (unorderedCollection.isNotEmpty()) {
            val startSize = unorderedCollection.size
            val itr = unorderedCollection.iterator()
            while (itr.hasNext()) {
                val o = itr.next()
                @Suppress("UNCHECKED_CAST")
                val enclosing = o.java.enclosingClass?.kotlin as KClass<out Mapper<*>>?
                val enclosingFound = enclosing == null || enclosing in ordered
                val dependencies = o.java.getAnnotation(DependsOn::class.java)?.mappers ?: emptyArray()
                val dependenciesFound = dependencies.all { it in ordered }
                if (enclosingFound && dependenciesFound) {
                    itr.remove()
                    ordered.add(o)
                }
            }
            check(startSize != unorderedCollection.size)
        }
        return ordered.asSequence()
    }
}