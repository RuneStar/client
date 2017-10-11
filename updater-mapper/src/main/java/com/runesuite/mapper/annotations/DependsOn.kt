package com.runesuite.mapper.annotations

import com.runesuite.mapper.Mapper
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Inherited
@Target(AnnotationTarget.CLASS)
annotation class DependsOn(
        vararg val mappers: KClass<out Mapper<*>>
)