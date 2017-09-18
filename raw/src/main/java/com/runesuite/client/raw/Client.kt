package com.runesuite.client.raw

import com.runesuite.client.raw.access.XClient

object Client : Wrapper() {

    override lateinit var accessor: XClient

    override fun toString(): String {
        return "Client"
    }
}