package org.runestar.client.api.game

import org.runestar.client.raw.access.XEnumType

inline class EnumType(val accessor: XEnumType) {

    val keys: IntArray get() = accessor.keys

    fun getInt(key: Int): Int {
        keys.forEachIndexed { i, k ->
            if (k == key) return accessor.intvals[i]
        }
        return accessor.defaultint
    }

    fun getString(key: Int): String {
        keys.forEachIndexed { i, k ->
            if (k == key) return accessor.strvals[i]
        }
        return accessor.defaultstr
    }
}