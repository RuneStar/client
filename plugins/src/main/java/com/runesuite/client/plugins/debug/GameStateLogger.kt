//package com.runesuite.client.plugins.debug
//
//import com.runesuite.client.dev.plugins.DisposablePlugin
//import com.runesuite.client.dev.plugins.Plugin
//import com.runesuite.client.game.live.Game
//
//class GameStateLogger : DisposablePlugin<Plugin.Settings>() {
//
//    override val defaultSettings = Plugin.Settings()
//
//    override fun start() {
//        super.start()
//        add(Game.stateChanges.subscribe {
//            logger.debug { it }
//        })
//    }
//}