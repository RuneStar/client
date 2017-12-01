package com.runesuite.client.plugins

open class PluginSettings {

    var enabled = false
        private set

    internal fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }
}