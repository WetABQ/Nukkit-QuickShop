package io.wetabq.quickshop.shop

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.item.Item
import io.wetabq.quickshop.event.PlayerSellEvent
import io.wetabq.quickshop.utils.Lang
import me.onebone.economyapi.EconomyAPI

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class SellShop(sign: String) : Shop(sign) {

    fun sellItem(player: Player, count: Int) {
        val money = EconomyAPI.getInstance().myMoney(shopData.owner)
        val price = shopData.price * count
        if (money >= price) {
            if (this.getShopChest() != null) {
                val item = Item.get(shopData.itemId, shopData.itemMeta, count)
                if (Shop.hasItem(player.inventory, item)) {
                    if (Shop.getItemInInventoryCount(player.inventory, item) >= count) {
                        if (!player.inventory.isFull && player.inventory.canAddItem(item)) {

                            val event = PlayerSellEvent(player, shopData,count)
                            Server.getInstance().pluginManager.callEvent(event)
                            if (event.isCancelled) return

                            EconomyAPI.getInstance().addMoney(player.name, price.toDouble())
                            if (!shopData.unlimited) {
                                EconomyAPI.getInstance().reduceMoney(shopData.owner, price.toDouble())
                                val owner = Server.getInstance().getPlayerExact(shopData.owner)
                                owner?.sendMessage(Lang.getMessage("&aPlayer &e{} &asuccessfully sold &6{}*{} &aand reduce &e{}$", arrayOf(player.name, Lang.getItemName(item), count,price)))
                                this.getShopChest()?.realInventory?.addItem(item)
                            }
                            player.inventory.removeItem(item)
                            player.sendMessage(Lang.getMessage("&aSuccessfully sold &6{}*{}", arrayOf(Lang.getItemName(item),"$count")))
                        } else {
                            player.sendMessage(Lang.getMessage("&cThe owner's chest is full!"))
                        }
                    } else {
                        player.sendMessage(Lang.getMessage("&cYour inventory is not enough to sell"))
                    }
                } else {
                    player.sendMessage(Lang.getMessage("&cYour inventory is not enough to sell"))
                }
            }
        } else {
            player.sendMessage(Lang.getMessage("&cThe owner money is not enough to buy"))
        }
    }

}