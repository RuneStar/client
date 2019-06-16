package org.runestar.client.game.api.live

import io.reactivex.Observable
import org.runestar.client.game.api.MenuOption
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XClient
import java.awt.Point
import java.awt.Rectangle

object Menu {

    const val OPTION_HEIGHT = 15

    val isOpen get() = CLIENT.isMiniMenuOpen

    val x get() = CLIENT.menuX

    val y get() = CLIENT.menuY

    val width get() = CLIENT.menuWidth

    val height get() = CLIENT.menuHeight

    val shape get() = Rectangle(x, y, width, height)

    var optionsCount: Int
        get() = CLIENT.menuOptionsCount
        set(value) { CLIENT.menuOptionsCount = value }

    val actions: Observable<MenuOption> = XClient.doAction.exit.map {
        MenuOption.of(
                it.arguments[2] as Int, it.arguments[3] as Int, it.arguments[0] as Int,
                it.arguments[1] as Int, it.arguments[5] as String, it.arguments[4] as String,
                false
        )
    }

    val openings: Observable<Point> = XClient.openMiniMenu.exit.map {
        Point(it.arguments[0] as Int, it.arguments[1] as Int)
    }

    val optionShapes: List<Rectangle> get() = when (isOpen) {
        true -> List(optionsCount) { getOptionShape(it) }
        false -> emptyList()
    }

    fun getOptionShape(index: Int): Rectangle =  Rectangle(x, y + (index + 1) * OPTION_HEIGHT + 3, width, OPTION_HEIGHT)

    val options: List<MenuOption> get() = List(optionsCount) { getOption(it) }

    fun getOption(index: Int): MenuOption {
        return MenuOption.of(
                CLIENT.menuOpcodes[index],
                CLIENT.menuArguments0[index],
                CLIENT.menuArguments1[index],
                CLIENT.menuArguments2[index],
                checkNotNull(CLIENT.menuTargetNames[index]),
                checkNotNull(CLIENT.menuActions[index]),
                CLIENT.menuShiftClick[index]
        )
    }

    fun setOption(index: Int, menuOption: MenuOption) {
        CLIENT.menuActions[index] = menuOption.action
        CLIENT.menuArguments0[index] = menuOption.argument0
        CLIENT.menuArguments1[index] = menuOption.argument1
        CLIENT.menuArguments2[index] = menuOption.argument2
        CLIENT.menuOpcodes[index] = menuOption.opcode
        CLIENT.menuShiftClick[index] = menuOption.shiftClick
        CLIENT.menuTargetNames[index] = menuOption.targetName
    }

    fun addOption(menuOption: MenuOption) {
        setOption(optionsCount++, menuOption)
    }

    val optionAdditions: Observable<MenuOption> = XClient.addMiniMenuEntry.exit.map {
        MenuOption.of(
                it.arguments[2] as Int, it.arguments[3] as Int, it.arguments[4] as Int, it.arguments[5] as Int,
                it.arguments[1] as String, it.arguments[0] as String, it.arguments[6] as Boolean
        )
    }

    override fun toString(): String {
        return "Menu(isOpen=$isOpen, optionsCount=$optionsCount, shape=$shape)"
    }
}