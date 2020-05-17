@file:JvmName("Client")

package org.runestar.client.raw

import org.runestar.client.raw.access.XClient
import java.util.ServiceLoader
import java.util.function.Supplier

/**
 * Singleton for the `client` [java.applet.Applet] class.
 */
@JvmField
val CLIENT: XClient = ServiceLoader.load(ClientProvider::class.java).single().get()

interface ClientProvider : Supplier<XClient>