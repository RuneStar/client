package org.runestar.client.updater.mapper

import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.CLASS)
annotation class MethodParameters(
        vararg val names: String = emptyArray()
)
