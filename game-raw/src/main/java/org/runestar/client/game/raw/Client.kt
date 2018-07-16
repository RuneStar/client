@file:JvmName("Client")

package org.runestar.client.game.raw

import org.runestar.client.game.raw.access.XClient
import java.util.*
import java.util.function.Supplier

/**
 * Singleton for the `client` [java.applet.Applet] class.
 */
@JvmField
val CLIENT: XClient = ServiceLoader.load(ClientProvider::class.java).single().get()

interface ClientProvider : Supplier<XClient>