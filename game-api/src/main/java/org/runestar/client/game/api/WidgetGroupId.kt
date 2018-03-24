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
        val items = WidgetParentId(id, 0)
    }

    object MinimapOrbs : WidgetGroupId {

        override val id = 160

        val xp = WidgetParentId(id, 1)

        val hp = WidgetParentId(id, 2)
        val hp_circle = WidgetParentId(id, 6)

        val pray = WidgetParentId(id, 12)
        val pray_circle = WidgetParentId(id, 16)

        val run = WidgetParentId(id, 20)
        val run_circle = WidgetParentId(id, 24)

        val spec = WidgetParentId(id, 28)
        val spec_circle = WidgetParentId(id, 31)
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