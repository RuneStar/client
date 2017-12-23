package org.runestar.client.game.api

interface WidgetGroupId {

    val id: Int

    object Bank : WidgetGroupId {
        override val id = 12
        val items = WidgetParentId(id, 12)
    }

    object BankInventory : WidgetGroupId {
        override val id = 15
    }

    object WorldSwitcher : WidgetGroupId {
        override val id = 69
    }

    object Inventory : WidgetGroupId {
        override val id = 149
    }

    object MinimapOrbs : WidgetGroupId {
        override val id = 160
    }

    object ViewportResizableOldSchoolBox : WidgetGroupId {
        override val id = 161
    }

    object ViewportResizableBottomLine : WidgetGroupId {
        override val id = 161
    }

    object Chat : WidgetGroupId {
        override val id = 541
    }

    object LogOut : WidgetGroupId {
        override val id = 182
    }

    object Equipment : WidgetGroupId {
        override val id = 387
    }

    object Welcome : WidgetGroupId {
        override val id = 378
    }

    object Prayer : WidgetGroupId {
        override val id = 541
    }

    object ViewportFixed : WidgetGroupId {
        override val id = 541
    }

    object CombatOptions : WidgetGroupId {
        override val id = 593
    }
}