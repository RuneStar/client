package com.runesuite.client.inject

import com.runesuite.client.updater.common.FieldHook

internal val FieldHook.getterName get() = "get${field.capitalize()}"

internal val FieldHook.setterName get() = "set${field.capitalize()}"