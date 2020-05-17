package org.runestar.client.api.game

import java.lang.StringBuilder

const val BR_TAG = "<br>"

const val GT_TAG = "<gt>"

const val LT_TAG = "<lt>"

fun imageTag(index: Int) = "<img=$index>"

fun appendImageTag(index: Int, dst: StringBuilder) {
    dst.append("<img=").append(index).append('>')
}

fun unescapeAngleBrackets(s: String): String {
    return s.replace(GT_TAG, ">").replace(LT_TAG, "<")
}

fun escapeAngleBrackets(s: String): String {
    return s.replace(">", GT_TAG).replace("<", LT_TAG)
}

fun unescapeSpaces(s: String): String {
    return s.replace('\u00a0', ' ')
}

private val TAG_REGEX = "<.*?>".toRegex()

fun removeTags(s: String): String = s.replace(TAG_REGEX, "")