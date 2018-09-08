package io.wetabq.quickshop.config

import io.wetabq.quickshop.QuickShop

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class MasterConfig : QuickShopConfig("config") {
    var title = "&l&eQuick&6Shop &r&c» &a"

    init {
        init()
    }

    override fun init() {
        if (!isEmpty()) {
            try {
                title = configSection.getString("title")
            } catch (e: Exception) {
                QuickShop.instance.logger.warning(e.message)
                QuickShop.instance.logger.error("Master Config(config.yml) while loading config")
            }

        } else {
            spawnDefaultConfig()
        }
    }

    override fun spawnDefaultConfig() {
        if (isEmpty()) {
            configSection["title"] = title
        }
        save()
        init()
    }

    override fun save() {
        if (!isEmpty()) {
            try {
                configSection.clear()
                configSection["title"] = title
                config.setAll(configSection)
                config.save()
            } catch (e: Exception) {
                QuickShop.instance.logger.warning(e.message)
                QuickShop.instance.logger.error("Master Config(config.yml) has an error while saving config")
            }

        } else {
            spawnDefaultConfig()
        }
    }
}