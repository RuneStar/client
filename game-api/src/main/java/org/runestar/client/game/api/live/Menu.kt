package org.runestar.client.game.api.live

import org.runestar.client.game.api.MenuOption
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.game.raw.access.XClient
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
        MenuOption.of(
                it.arguments[2] as Int, it.arguments[3] as Int, it.arguments[0] as Int,
                it.arguments[1] as Int, it.arguments[5] as String, it.arguments[4] as String
        )
    }

    val openings: Observable<Point> = XClient.openMenu.exit.map {
        Point(it.arguments[0] as Int, it.arguments[1] as Int)
    }

    val optionShapes: List<Rectangle> get() = when (isOpen) {
        true -> List(optionsCount) { Rectangle(x, y + (it + 1) * OPTION_HEIGHT + 3, width, OPTION_HEIGHT) }
        false -> emptyList()
    }

    val options: List<MenuOption> get() = List(optionsCount) { getOption(it) }

    fun getOption(index: Int): MenuOption {
        val i = optionsCount - index - 1
        require(i >= 0)
        return MenuOption.of(
                accessor.menuOpcodes[i],
                accessor.menuArguments0[i],
                accessor.menuArguments1[i],
                accessor.menuArguments2[i],
                checkNotNull(accessor.menuTargetNames[i]),
                checkNotNull(accessor.menuActions[i])
        )
    }

    override fun toString(): String {
        return "Menu(isOpen=$isOpen, optionsCount=$optionsCount, shape=$shape)"
    }
}