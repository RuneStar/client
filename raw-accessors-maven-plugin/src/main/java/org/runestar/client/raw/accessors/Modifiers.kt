package org.runestar.client.raw.accessors

import java.lang.reflect.Modifier

internal fun classModifiersToString(modifiers: Int): String {
    return if (Modifier.isInterface(modifiers)) {
        Modifier.toString(Modifier.interfaceModifiers() and modifiers) + " interface"
    } else {
        Modifier.toString(Modifier.classModifiers() and modifiers) + " class"
    }
}

internal fun fieldModifiersToString(modifiers: Int): String {
    return Modifier.toString(Modifier.fieldModifiers() and modifiers) + " field"
}

internal fun methodModifiersToString(modifiers: Int): String {
    return Modifier.toString(Modifier.methodModifiers() and modifiers) + " method"
}

internal fun constructorModifiersToString(modifiers: Int): String {
    return Modifier.toString(Modifier.constructorModifiers() and modifiers) + " constructor"
}