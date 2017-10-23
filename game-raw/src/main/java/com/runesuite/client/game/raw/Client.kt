package com.runesuite.client.game.raw

import com.runesuite.client.game.raw.access.XClient

/**
 * Singleton for the "client" class
 */
object Client : Wrapper() {

    override lateinit var accessor: XClient

    override fun toString(): String {
        return "Client"
    }
}