package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XEnumType

inline class EnumType(val accessor: XEnumType) {

    fun getInt(key: Int): Int {
        accessor.keys.forEachIndexed { i, k ->
            if (k == key) return accessor.intvals[i]
        }
        return accessor.defaultint
    }

    fun getString(key: Int): String {
        accessor.keys.forEachIndexed { i, k ->
            if (k == key) return accessor.strvals[i]
        }
        return accessor.defaultstr
    }
}