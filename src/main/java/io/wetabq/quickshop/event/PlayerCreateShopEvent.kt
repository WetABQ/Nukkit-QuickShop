package io.wetabq.quickshop.event

import cn.nukkit.Player
import cn.nukkit.event.HandlerList
import cn.nukkit.event.player.PlayerEvent
import io.wetabq.quickshop.data.ShopData

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class PlayerCreateShopEvent(player : Player,shopData: ShopData) : PlayerEvent() {

    init {
        super.player = player
    }

    companion object {

        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlers(): HandlerList {
            return handlers
        }

    }

}