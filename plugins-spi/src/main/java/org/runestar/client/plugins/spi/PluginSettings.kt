package org.runestar.client.plugins.spi

/**
 * Subclasses should only add immutable properties.
 */
open class PluginSettings {

    var enabled = false
        internal set
}