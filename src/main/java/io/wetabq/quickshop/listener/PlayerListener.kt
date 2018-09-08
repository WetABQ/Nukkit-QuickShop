package io.wetabq.quickshop.listener

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.item.Item
import cn.nukkit.level.Position
import io.wetabq.quickshop.QuickShop

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class PlayerListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        for (shopData in QuickShop.instance.shopConfig.shopData.values) {
            QuickShop.addItemEntity(event.player, Position(shopData.chestX.toDouble(),shopData.chestY.toDouble(),shopData.chestZ.toDouble()), Item.get(shopData.itemId,shopData.itemMeta,1))
        }
    }

}