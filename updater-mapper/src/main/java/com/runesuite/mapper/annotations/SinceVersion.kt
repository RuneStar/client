package com.runesuite.mapper.annotations

import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.CLASS)
annotation class SinceVersion(
        val version: Int
)