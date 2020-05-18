package org.runestar.client.api.game.live

import io.reactivex.rxjava3.core.Observable
import org.runestar.client.api.game.MiniMenuOption
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XClient
import java.awt.Point
import java.awt.Rectangle

object MiniMenu {

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

    val actions: Observable<MiniMenuOption> = XClient.doAction.exit.map {
        MiniMenuOption.of(
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

    val options: List<MiniMenuOption> get() = List(optionsCount) { getOption(it) }

    fun getOption(index: Int): MiniMenuOption {
        return MiniMenuOption.of(
                CLIENT.menuOpcodes[index],
                CLIENT.menuArguments0[index],
                CLIENT.menuArguments1[index],
                CLIENT.menuArguments2[index],
                checkNotNull(CLIENT.menuTargetNames[index]),
                checkNotNull(CLIENT.menuActions[index]),
                CLIENT.menuShiftClick[index]
        )
    }

    fun setOption(index: Int, menuOption: MiniMenuOption) {
        CLIENT.menuActions[index] = menuOption.action
        CLIENT.menuArguments0[index] = menuOption.argument0
        CLIENT.menuArguments1[index] = menuOption.argument1
        CLIENT.menuArguments2[index] = menuOption.argument2
        CLIENT.menuOpcodes[index] = menuOption.opcode
        CLIENT.menuShiftClick[index] = menuOption.shiftClick
        CLIENT.menuTargetNames[index] = menuOption.targetName
    }

    fun addOption(menuOption: MiniMenuOption) {
        setOption(optionsCount++, menuOption)
    }

    val optionAdditions: Observable<MiniMenuOption> = XClient.addMiniMenuEntry.exit.map {
        MiniMenuOption.of(
                it.arguments[2] as Int, it.arguments[3] as Int, it.arguments[4] as Int, it.arguments[5] as Int,
                it.arguments[1] as String, it.arguments[0] as String, it.arguments[6] as Boolean
        )
    }

    override fun toString(): String {
        return "MiniMenu(isOpen=$isOpen, optionsCount=$optionsCount, shape=$shape)"
    }
}