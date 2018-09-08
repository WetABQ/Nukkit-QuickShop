package io.wetabq.quickshop.shop

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.Block
import cn.nukkit.blockentity.BlockEntityChest
import cn.nukkit.blockentity.BlockEntitySign
import cn.nukkit.inventory.BaseInventory
import cn.nukkit.item.Item
import cn.nukkit.level.Level
import cn.nukkit.level.Position
import cn.nukkit.math.Vector3
import io.wetabq.quickshop.QuickShop
import io.wetabq.quickshop.data.ShopData
import io.wetabq.quickshop.event.PlayerCreateShopEvent

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
abstract class Shop(sign: String) {

    val chestPos: Position
    val signPos: Position
    val level: Level
    val shopData: ShopData

    init {
        shopData = QuickShop.instance.shopConfig.shopData[sign]!!
        level = Server.getInstance().getLevelByName(shopData.level)
        if (level == null) {
            throw NullPointerException()
        }
        chestPos = Position(shopData.chestX.toDouble(),shopData.chestY.toDouble(),shopData.chestZ.toDouble(),level)
        signPos = Position(shopData.signX.toDouble(),shopData.chestY.toDouble(),shopData.signZ.toDouble(),level)
    }

    fun getShopChest(): BlockEntityChest? {
        val chestEntity = level.getBlockEntity(chestPos)
        return when(chestEntity is BlockEntityChest) {
            true -> chestEntity as BlockEntityChest
            false -> null
        }
    }

    fun getShopSign(): BlockEntitySign? {
        val signEntity = level.getBlockEntity(signPos)
        return when(signEntity is BlockEntitySign) {
            true -> signEntity as BlockEntitySign
            false -> null
        }
    }

    companion object {

        @JvmStatic
        fun hasItem(baseInventory: BaseInventory,item: Item): Boolean {
            var flag = false
            baseInventory.slots.values.forEach {i ->
                if (i.id == item.id && i.damage == item.damage) {
                    flag = true
                }
            }
            return flag
        }

        @JvmStatic
        fun getItemInInventory(baseInventory: BaseInventory,item: Item): Item? {
            baseInventory.slots.values.forEach {i ->
                if (i.id == item.id && i.damage == item.damage) {
                    return i
                }
            }
            return null
        }

        @JvmStatic
        fun createShop(owner: Player, chest: Position, sign: Position, price: Int, item: Item,type: Int) {
            val shopData = ShopData(owner.name,type,price,chest.x.toInt(),chest.y.toInt(),chest.z.toInt(),sign.x.toInt(),sign.z.toInt(),chest.level.folderName,item.id,item.damage,false)

            val event = PlayerCreateShopEvent(owner, shopData)
            Server.getInstance().pluginManager.callEvent(event)

            QuickShop.instance.shopConfig.addShop(shopData)
        }

        @JvmStatic
        fun findShop(chest: Block): Pair<BlockEntitySign,ShopData?>? {
            var sign: BlockEntitySign? = null
            for (x in chest.x.toInt() - 1..chest.x.toInt() + 1) {
                val getSign = chest.level.getBlockEntity(Vector3(x.toDouble(), chest.y, chest.z))
                if (getSign != null && getSign is BlockEntitySign) {
                    sign = getSign
                    break
                }
            }
            if (sign == null) {
                for (z in chest.z.toInt() - 1..chest.z.toInt() + 1) {
                    val getSign = chest.level.getBlockEntity(Vector3(chest.x, chest.y, z.toDouble()))
                    if (getSign != null && getSign is BlockEntitySign) {
                        sign = getSign
                        break
                    }
                }
            }
            if (sign != null) {
                val key = "${sign.x.toInt()}:${sign.y.toInt()}:${sign.z.toInt()}:${sign.level.folderName}"
                val shopData = QuickShop.instance.shopConfig.shopData[key]
                if (shopData != null) {
                    return Pair(sign,shopData)
                } else {
                    return Pair(sign,null)
                }
            }
            return null
        }

    }

}