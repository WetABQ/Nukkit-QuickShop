package io.wetabq.quickshop.config

import io.wetabq.quickshop.QuickShop
import io.wetabq.quickshop.data.ShopData

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class ShopConfig : QuickShopConfig("shop") {

    var shopData = hashMapOf<String,ShopData>()
    /*
    "233:666:233:world" =>
    "{
        owner: "WetABQ",
        chestX: 233,
        chestY: 666,
        chestZ: 233,
        signX : 233,
        signZ : 234,
        level: "world",
        itemId: 123,
        itemMeta: 1,
        unlimited: false
    }"
     */

    init {
        init()
    }

    override fun init() {
        if (!isEmpty()) {
            try {
                val shopMapString = configSection["shops"] as Map<*, *>
                shopMapString.forEach { k, value ->
                    val v = value as Map<*,*>
                    shopData[k.toString()] = ShopData(v["owner"].toString(),
                            v["type"].toString().toInt(),v["price"].toString().toInt(),
                            v["chestX"]!!.toString().toInt(),v["chestY"]!!.toString().toInt(),v["chestZ"]!!.toString().toInt(),
                            v["signX"]!!.toString().toInt(),v["signZ"]!!.toString().toInt(),
                            v["level"]!!.toString(),
                            v["itemId"].toString().toInt(),v["itemMeta"].toString().toInt(),
                            v["unlimited"].toString().toBoolean())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                QuickShop.instance.logger.error("Shop Config(shop.yml) while loading config")
            }

        } else {
            spawnDefaultConfig()
        }
    }

    override fun spawnDefaultConfig() {
        if (isEmpty()) {
            configSection["shops"] = hashMapOf<String,Map<String,String>>()
        }
        init()
        save()
    }

    fun addShop(shop: ShopData) {
        shopData["${shop.signX}:${shop.chestY}:${shop.signZ}:${shop.level}"] = shop
        save()
    }

    fun removeShop(signPos: String) {
        shopData.remove(signPos)
        save()
    }

    override fun save() {
        if (!isEmpty()) {
            try {
                configSection.clear()
                val shopMapString = hashMapOf<String,Map<String,*>>()
                shopData.forEach{ k,v -> shopMapString[k] = v.toMap()}
                configSection["shops"] = shopMapString
                config.setAll(configSection)
                config.save()
            } catch (e: Exception) {
                e.printStackTrace()
                QuickShop.instance.logger.error("Shop Config(shop.yml) has an error while saving config")
            }

        } else {
            spawnDefaultConfig()
        }
    }

}