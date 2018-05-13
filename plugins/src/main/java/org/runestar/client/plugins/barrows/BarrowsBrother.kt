package org.runestar.client.plugins.barrows

import org.runestar.client.game.api.GlobalRectangle

enum class BarrowsBrother(val digArea: GlobalRectangle) {

    VERAC(GlobalRectangle(3555, 3295, 5, 6, 0)),

    DHAROK(GlobalRectangle(3572, 3295, 6, 6, 0)),

    AHRIM(GlobalRectangle(3562, 3286, 6, 7, 0)),

    TORAG(GlobalRectangle(3552, 3281, 5, 4, 0)),

    GUTHAN(GlobalRectangle(3574, 3280, 7, 5, 0)),

    KARIL(GlobalRectangle(3563, 3273, 7, 7, 0));

    companion object {
        @JvmField val VALUES = values().asList()
    }
}