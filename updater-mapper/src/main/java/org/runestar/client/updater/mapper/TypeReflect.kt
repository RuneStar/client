package org.runestar.client.updater.mapper

import org.objectweb.asm.Type
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

val Class<*>.type: Type get() = Type.getType(this)
val KClass<*>.type: Type get() = javaObjectType.type

val KClass<*>.primitiveType: Type get() = checkNotNull(javaPrimitiveType, { this }).type

val Method.type: Type get() = Type.getType(this)
val KFunction<*>.type: Type get() = checkNotNull(javaMethod, { this }).type

val Method.signature: Pair<String, List<Type>> get() = name to type.argumentTypes.asList()
val KFunction<*>.signature: Pair<String, List<Type>> get() = checkNotNull(javaMethod, { this }).signature

val Method.mark: Pair<String, Type> get() = name to type
val KFunction<*>.mark: Pair<String, Type> get() = checkNotNull(javaMethod, { this }).mark

val Method.id: Triple<Type, String, Type> get() = Triple(declaringClass.type, name, type)
val KFunction<*>.id: Triple<Type, String, Type> get() = checkNotNull(javaMethod, { this }).id