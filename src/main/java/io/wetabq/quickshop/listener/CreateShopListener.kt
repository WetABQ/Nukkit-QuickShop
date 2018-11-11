package io.wetabq.quickshop.listener

import cn.nukkit.Server
import cn.nukkit.block.Block
import cn.nukkit.blockentity.BlockEntitySign
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerChatEvent
import cn.nukkit.event.player.PlayerInteractEvent
import cn.nukkit.item.Item
import io.wetabq.quickshop.QuickShop
import io.wetabq.quickshop.shop.Shop
import io.wetabq.quickshop.shop.ShopType
import io.wetabq.quickshop.utils.Lang

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class CreateShopListener : Listener {

    private val createShopPlayer = hashMapOf<String,Pair<Long,Block>>()

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.block.id == Block.CHEST) {
            val pair = Shop.findShop(event.block)
            val sign: BlockEntitySign? = pair?.first
            if (sign != null && pair.second == null && event.item.id != Item.SIGN && event.item.id != 0) {
                createShopPlayer[event.player.name] = Pair(System.currentTimeMillis() + 8000,event.block)
                event.player.sendMessage(Lang.getMessage("Please enter the &e&lprice &r&ato set up your shop!"))
                event.setCancelled()
            }
        }
    }

    @EventHandler
    fun onChat(event: PlayerChatEvent) {
        if (createShopPlayer.containsKey(event.player.name)) {
            if (createShopPlayer[event.player.name]!!.first < System.currentTimeMillis() || !QuickShop.isInteger(event.message)) {
                createShopPlayer.remove(event.player.name)
                return
            }
            try {
                val chest = createShopPlayer[event.player.name]!!.second
                val player = event.player
                val sign: BlockEntitySign? = Shop.findShop(chest)?.first
                if (sign != null) {
                    val item = player.inventory.itemInHand
                    sign.setText(Lang.getMessage("&c[&l&eQuick&6Shop&r&c]",false),
                            Lang.getMessage("&aType: {}", arrayOf("BUY"),false),
                            Lang.getMessage("&eItem: {}", arrayOf(Lang.getItemName(item)),false),
                            Lang.getMessage("&c&lPrice: {}$ /count", arrayOf(event.message),false))
                    QuickShop.addItemEntity(chest, Item.get(item.id,item.damage,1),(sign.x.toInt()+sign.z.toInt()).toLong())
                    player.sendMessage(Lang.getMessage("Successfully created the shop"))
                    Shop.createShop(player, chest, sign, event.message.toInt(), item, ShopType.BUY)
                } else {
                    player.sendMessage(Lang.getMessage("&cPlease put a sign around your chest."))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Server.getInstance().logger.error("An error occurred while creating the shop")
            }
            event.setCancelled()
            createShopPlayer.remove(event.player.name)
        }
    }

}