package io.wetabq.quickshop.data

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class ShopData(var owner: String,var type: Int,var price: Int,
               var chestX: Int,var chestY: Int,var chestZ: Int,
               var signX: Int,var signZ: Int,
               var level: String,
               var itemId: Int,var itemMeta: Int,
               var unlimited: Boolean) {

    fun toMap() : Map<String,Any>{
        return hashMapOf(
                "owner" to owner,
                "type" to type,
                "price" to price,
                "chestX" to chestX,
                "chestY" to chestY,
                "chestZ" to chestZ,
                "signX" to signX,
                "signZ" to signZ,
                "level" to level,
                "itemId" to itemId,
                "itemMeta" to itemMeta,
                "unlimited" to unlimited)
    }

    override fun toString(): String {
        return toMap().toString()
    }

}