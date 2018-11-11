package io.wetabq.quickshop.listener

import cn.nukkit.Server
import cn.nukkit.block.BlockChest
import cn.nukkit.block.BlockWallSign
import cn.nukkit.blockentity.BlockEntitySign
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.block.BlockBreakEvent
import cn.nukkit.event.player.PlayerChatEvent
import cn.nukkit.event.player.PlayerInteractEvent
import cn.nukkit.item.Item
import io.wetabq.quickshop.QuickShop
import io.wetabq.quickshop.event.PlayerRemoveShopEvent
import io.wetabq.quickshop.shop.BuyShop
import io.wetabq.quickshop.shop.SellShop
import io.wetabq.quickshop.shop.Shop
import io.wetabq.quickshop.shop.ShopType
import io.wetabq.quickshop.utils.Lang

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class InteractionShopListener : Listener {

    companion object {
        val interactShop = hashMapOf<String, Pair<Long, Shop>>()
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.block is BlockWallSign) {
            val block = event.block
            val player = event.player
            val shopData = QuickShop.instance.shopConfig.shopData["${block.x.toInt()}:${block.y.toInt()}:${block.z.toInt()}:${block.level.folderName}"]
            if (shopData != null) {
                val shop = if(shopData.type == ShopType.BUY) BuyShop("${block.x.toInt()}:${block.y.toInt()}:${block.z.toInt()}:${block.level.folderName}") else SellShop("${block.x.toInt()}:${block.y.toInt()}:${block.z.toInt()}:${block.level.folderName}")
                player.sendMessage(Lang.getMessage(
                        "&d+-------INFO-------+\n" +
                        "&d| &aOwner: &e{}\n" +
                        "&d| &aItem: &e{}\n" +
                        "&d| &aPrice: &e{}$ /count\n" +
                        "&d| &aThis shop is &b{} &aitems\n" +
                        "&d+------------------+"
                ,arrayOf(shopData.owner,Lang.getItemName(Item.get(shopData.itemId,shopData.itemMeta)),shopData.price,if(shopData.type == ShopType.BUY) "SELLING" else "BUYING")))
                player.sendMessage(Lang.getMessage("Please enter the &l&ecount &r&ayou need to &d&l{}", arrayOf(if(shopData.type == ShopType.BUY) "BUY" else "SELL")))
                interactShop[player.name] = Pair(System.currentTimeMillis() + 8000, shop)
            }
        }
    }

    @EventHandler
    fun onChat(event: PlayerChatEvent) {
        if (interactShop.containsKey(event.player.name)) {
            if (interactShop[event.player.name]!!.first < System.currentTimeMillis() || !QuickShop.isInteger(event.message)) {
                interactShop.remove(event.player.name)
                return
            }
            try {
                val player = event.player
                val shop = interactShop[player.name]!!.second
                if (shop is SellShop) {
                    shop.sellItem(player, event.message.toInt())
                } else if (shop is BuyShop) {
                    shop.buyItem(player, event.message.toInt())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Server.getInstance().logger.error("An error occurred while using the buy/sell shop")
            }
            event.setCancelled()
            interactShop.remove(event.player.name)
        }
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val player = event.player
        if (event.block is BlockWallSign) {
            val block = event.block
            val key = "${block.x.toInt()}:${block.y.toInt()}:${block.z.toInt()}:${block.level.folderName}"
            val shopData = QuickShop.instance.shopConfig.shopData[key]
            if (shopData != null) {
                if (shopData.owner == player.name || player.isOp) {
                    player.sendMessage(Lang.getMessage("Successfully removed your shop"))

                    val e = PlayerRemoveShopEvent(player, shopData)
                    Server.getInstance().pluginManager.callEvent(e)
                    if (e.isCancelled){
                        event.setCancelled()
                        return
                    }

                    QuickShop.removeItemEntity((shopData.signX+shopData.signZ).toLong())
                    QuickShop.instance.shopConfig.removeShop(key)
                } else {
                    event.setCancelled()
                }
            }
        } else if (event.block is BlockChest) {
            val chest = event.block
            val sign: BlockEntitySign? = Shop.findShop(chest)?.first
            if (sign != null) {
                val key = "${sign.x.toInt()}:${sign.y.toInt()}:${sign.z.toInt()}:${sign.level.folderName}"
                val shopData = QuickShop.instance.shopConfig.shopData[key]
                if (shopData != null) {
                    if (shopData.owner == player.name || player.isOp) {
                        player.sendMessage(Lang.getMessage("Successfully removed your shop"))

                        val e = PlayerRemoveShopEvent(player, shopData)
                        Server.getInstance().pluginManager.callEvent(e)
                        if (e.isCancelled){
                            event.setCancelled()
                            return
                        }

                        QuickShop.removeItemEntity((shopData.signX+shopData.signZ).toLong())
                        QuickShop.instance.shopConfig.removeShop(key)
                    } else {
                        event.setCancelled()
                    }
                }
            }
        }
    }

}