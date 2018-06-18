package org.runestar.client.game.raw

import org.runestar.client.game.raw.access.XClient
import java.util.function.Supplier

interface ClientProvider : Supplier<XClient>