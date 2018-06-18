package org.runestar.client.game.raw

import org.runestar.client.game.raw.access.XClient
import java.util.*

/**
 * Singleton for the `client` [java.applet.Applet] class.
 */
object Client {

    // The XClient class is generated during the build process

    val accessor: XClient = ServiceLoader.load(ClientProvider::class.java).single().get()
}