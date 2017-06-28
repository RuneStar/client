package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.base.access.XClient
import com.runesuite.client.game.MenuOption
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

    val openings: Observable<Point> = XClient.openMenu.exit.map { Point(it.arguments[0] as Int, it.arguments[1] as Int) }

    val optionShapes: List<Rectangle>
        get() = when (isOpen) {
            true -> 1.rangeTo(optionsCount).map { Rectangle(x, y + it * OPTION_HEIGHT + 3, width, OPTION_HEIGHT) }
            false -> emptyList()
        }

    val options: List<MenuOption>
        get() = (optionsCount - 1).downTo(0).map {
                MenuOption.of(
                        accessor.menuOpcodes[it],
                        accessor.menuArguments0[it],
                        accessor.menuArguments1[it],
                        accessor.menuArguments2[it],
                        accessor.menuTargetNames[it]!!,
                        accessor.menuActions[it]!!
                )
        }

    override fun toString(): String {
        return "Menu(isOpen=$isOpen, optionsCount=$optionsCount, shape=$shape)"
    }
}