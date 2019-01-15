package org.runestar.client.game.api

fun unescapeAngleBrackets(s: String): String {
    return s.replace("<gt>", ">").replace("<lt>", "<")
}

fun escapeAngleBrackets(s: String): String {
    return s.replace(">", "<gt>").replace("<", "<lt>")
}

fun unescapeSpaces(s: String): String {
    return s.replace('\u00a0', ' ')
}