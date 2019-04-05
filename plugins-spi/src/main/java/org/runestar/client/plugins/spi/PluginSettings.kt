package org.runestar.client.plugins.spi

open class PluginSettings {

    var enabled = false
        internal set

    @Transient lateinit var write: () -> Unit
        internal set
}