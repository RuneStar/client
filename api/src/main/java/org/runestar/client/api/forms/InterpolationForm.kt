package org.runestar.client.api.forms

import java.awt.RenderingHints

enum class InterpolationForm(val renderingHintValue: Any) {
    NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR),
    BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
    BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC),
}