package com.runesuite.mapper

import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import com.runesuite.mapper.extensions.type
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

data class IdClass(
        val `class`: String,
        val name: String,
        val `super`: String?,
        val access: Int,
        val interfaces: List<String>?,
        val fields: List<IdField>?,
        val methods: List<IdMethod>?
)

data class IdField(
        val field: String,
        val owner: String?,
        val name: String,
        val access: Int,
        val descriptor: String
)

data class IdMethod(
        val method: String,
        val owner: String?,
        val name: String,
        val access: Int,
        val parameters: List<String>?,
        val descriptor: String
)

fun Mapper.Context.buildIdHierarchy(): List<IdClass> {
    val tclasses = ArrayList<IdClass>()
    classes.forEach { k, v ->
        val tfields = ArrayList<IdField>()
        val tmethods = ArrayList<IdMethod>()
        @Suppress("UNCHECKED_CAST")
        k.nestedClasses.forEach { n ->
            fields[n as KClass<out Mapper<Field2>>]?.let { f ->
                val owner = f.klass.name.takeUnless { it == v.name }
                tfields.add(IdField(n.simpleName!!, owner, f.name, f.access, f.desc))
            }
            methods[n as KClass<out Mapper<Method2>>]?.let { m ->
                val ps = n.findAnnotation<MethodParameters>()?.names?.asList()
                val owner = m.klass.name.takeUnless { it == v.name }
                tmethods.add(IdMethod(n.simpleName!!, owner, m.name, m.access, ps, m.desc))
            }
        }
        val superName = if (v.superType == Any::class.type) null else v.superType.className
        tclasses.add(IdClass(k.simpleName!!, v.name, superName, v.access, v.interfaces.map { it.className }, tfields, tmethods))
    }
    return tclasses
}