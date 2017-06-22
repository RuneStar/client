package com.runesuite.client.base

import com.runesuite.client.base.access.XClient

object Client : Wrapper() {

    override lateinit var accessor: XClient

    override fun toString(): String {
        return "Client"
    }
}