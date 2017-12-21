package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.MenuOption
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.game.raw.access.XClient
import io.reactivex.Observable
import java.awt.Point
import java.awt.Rectangle

object Menu {

    const val OPTION_HEIGHT = 15

    val isOpen get() = accessor.isMenuOpen

    val x get() = accessor.menuX

    val y get() = accessor.menuY

    val width get() = accessor.menuWidth

    val height get() = accessor.menuHeight

    val shape get() = Rectangle(x, y, width, height)

    val optionsCount get() = accessor.menuOptionsCount

    val actions: Observable<MenuOption> = XClient.menuAction.exit.map {
        MenuOption.of(it.arguments[2] as Int, it.arguments[3] as Int, it.arguments[0] as Int,
                it.arguments[1] as Int, it.arguments[5] as String, it.arguments[4] as String)
    }

    val openings: Observable<Point> = XClient.openMenu.exit.map {
        Point(it.arguments[0] as Int, it.arguments[1] as Int)
    }

    val optionShapes: List<Rectangle> get() = when (isOpen) {
        true -> List(optionsCount) { Rectangle(x, y + (it + 1) * OPTION_HEIGHT + 3, width, OPTION_HEIGHT) }
        false -> emptyList()
    }

    val options: List<MenuOption> get() = List(optionsCount) {
        MenuOption.of(
                accessor.menuOpcodes[it],
                accessor.menuArguments0[it],
                accessor.menuArguments1[it],
                accessor.menuArguments2[it],
                checkNotNull(accessor.menuTargetNames[it]),
                checkNotNull(accessor.menuActions[it])
        )
    }.asReversed()

    override fun toString(): String {
        return "Menu(isOpen=$isOpen, optionsCount=$optionsCount, shape=$shape)"
    }
}