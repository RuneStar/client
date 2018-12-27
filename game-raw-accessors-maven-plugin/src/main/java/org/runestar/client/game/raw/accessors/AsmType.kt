package org.runestar.client.game.raw.accessors

import org.objectweb.asm.Type

internal val Type.baseType get() = if (sort == Type.ARRAY) elementType else this

internal val Type.arrayDimensions get() = if (sort == Type.ARRAY) dimensions else 0