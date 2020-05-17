package org.runestar.client.api.plugins

open class PluginSettings {

    var enabled = false
        internal set

    @Transient lateinit var write: () -> Unit
        internal set
}