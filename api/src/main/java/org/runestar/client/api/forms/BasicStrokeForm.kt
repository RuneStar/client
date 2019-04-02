package org.runestar.client.api.forms

import java.awt.BasicStroke

data class BasicStrokeForm(
        val width: Float
) {

    @Transient val value: BasicStroke = BasicStroke(width)
}