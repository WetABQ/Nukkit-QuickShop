package io.wetabq.quickshop.config

import io.wetabq.quickshop.QuickShop

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class ItemNameConfig : QuickShopConfig("itemname") {
    var itemNameConfig = hashMapOf(
            "#example \"1\" => \"Normal Stone\"" to ""
    )

    init {
        init()
    }

    override fun init() {
        if (!isEmpty()) {
            try {
                itemNameConfig = configSection.get("itemName") as HashMap<String,String>
            } catch (e: Exception) {
                QuickShop.instance.logger.warning(e.message)
                QuickShop.instance.logger.error("ItemName Config(itemname.yml) while loading config")
            }

        } else {
            spawnDefaultConfig()
        }
    }

    override fun spawnDefaultConfig() {
        if (isEmpty()) {
            configSection["itemName"] = itemNameConfig
        }
        save()
        init()
    }

    override fun save() {
        if (!isEmpty()) {
            try {
                configSection.clear()
                configSection["itemName"] = itemNameConfig
                config.setAll(configSection)
                config.save()
            } catch (e: Exception) {
                QuickShop.instance.logger.warning(e.message)
                QuickShop.instance.logger.error("ItemName Config(itemname.yml) has an error while saving config")
            }

        } else {
            spawnDefaultConfig()
        }
    }
}