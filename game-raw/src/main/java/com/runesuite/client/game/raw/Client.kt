package com.runesuite.client.game.raw

import com.runesuite.client.game.raw.access.XClient

/**
 * Singleton for the `client` [java.applet.Applet] class.
 */
object Client : Wrapper() {

    // The XClient class is generated during the build process

    override lateinit var accessor: XClient

    /**
     * @return `"Client"`
     */
    override fun toString(): String {
        return "Client"
    }
}