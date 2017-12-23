package org.runestar.client.updater.mapper

import com.google.common.reflect.ClassPath
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Jar2
import java.nio.file.Path
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf

open class JarMapper(vararg val classMappers: KClass<out Mapper<Class2>>) {

    @Suppress("UNCHECKED_CAST")
    constructor(pkg: String, classLoader: ClassLoader) : this(
            *ClassPath.from(classLoader)
                    .getTopLevelClassesRecursive(pkg)
                    .mapNotNull { it.load().kotlin }
                    .filter { it.isSubclassOf(Mapper::class) && it.isSubclassOf(ElementMatcher.Class::class) }
                    .map { it as KClass<out Mapper<Class2>> }
                    .toTypedArray()
    )

    fun map(jar: Path, context: Mapper.Context, version: Int = 0) {
        val jar2 = Jar2(jar)
        @Suppress("UNCHECKED_CAST")
        val unordered = classMappers.asSequence()
                .filter { it.findJavaAnnotation<SinceVersion>()?.version ?: 0 <= version }
                .flatMap { it.nestedClasses.asSequence().plus(it) }
                .filter { it.isSubclassOf(Mapper::class) }
                .map { it as KClass<out Mapper<*>> }
                .filter { it.findJavaAnnotation<SinceVersion>()?.version ?: 0 <= version }
        val ordered = orderDependencies(unordered)
        ordered.map { it.createInstance() }.forEach {
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
                val dependencies = o.findJavaAnnotation<DependsOn>()?.mappers ?: emptyArray()
                val dependenciesFound = dependencies.all { it in ordered }
                if (enclosingFound && dependenciesFound) {
                    itr.remove()
                    ordered.add(o)
                }
            }
            val endSize = unorderedCollection.size
            check(startSize != endSize) {
                "Unable to resolve dependencies\nUnordered: $unorderedCollection\nOrdered: $ordered"
            }
        }
        return ordered.asSequence()
    }

    private inline fun <reified T: Annotation> KClass<*>.findJavaAnnotation(): T? {
        return this.java.getAnnotation(T::class.java)
    }
}