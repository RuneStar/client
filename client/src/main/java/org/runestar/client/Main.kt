@file:JvmName("Main")

package org.runestar.client

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import org.kxtra.slf4j.loggerfactory.getLogger
import org.runestar.client.api.Application

fun main(args: Array<String>) {
    val rootLogger = getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    rootLogger.level = Level.toLevel(System.getProperty("runestar.log.level"))

    Application.start()
}