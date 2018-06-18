package org.runestar.client.game.api.live

import io.reactivex.Observable
import org.runestar.client.game.api.MenuOption
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.CLIENT
import java.awt.Point
import java.awt.Rectangle

object Menu {

    const val OPTION_HEIGHT = 15

    val isOpen get() = CLIENT.isMenuOpen

    val x get() = CLIENT.menuX

    val y get() = CLIENT.menuY

    val width get() = CLIENT.menuWidth

    val height get() = CLIENT.menuHeight

    val shape get() = Rectangle(x, y, width, height)

    val optionsCount get() = CLIENT.menuOptionsCount

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
                CLIENT.menuOpcodes[i],
                CLIENT.menuArguments0[i],
                CLIENT.menuArguments1[i],
                CLIENT.menuArguments2[i],
                checkNotNull(CLIENT.menuTargetNames[i]),
                checkNotNull(CLIENT.menuActions[i])
        )
    }

    val optionAdditions: Observable<MenuOption> = XClient.insertMenuItem.exit.map {
        MenuOption.of(
                it.arguments[2] as Int, it.arguments[3] as Int, it.arguments[4] as Int, it.arguments[5] as Int,
                it.arguments[1] as String, it.arguments[0] as String
        )
    }

    override fun toString(): String {
        return "Menu(isOpen=$isOpen, optionsCount=$optionsCount, shape=$shape)"
    }
}