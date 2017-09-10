package com.runesuite.client.core.raw

import com.runesuite.client.core.raw.access.XClient

object Client : Wrapper() {

    override lateinit var accessor: XClient

    override fun toString(): String {
        return "Client"
    }
}