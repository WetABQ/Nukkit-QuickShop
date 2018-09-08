package io.wetabq.quickshop.config

import cn.nukkit.utils.Config
import cn.nukkit.utils.ConfigSection
import io.wetabq.quickshop.QuickShop

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
abstract class QuickShopConfig(configname: String) : QuickShopConfigInterface {
    protected var config: Config
    protected var configSection: ConfigSection

    init {
        this.config = Config(  "${QuickShop.instance.dataFolder}/$configname.yml", Config.YAML)
        this.configSection = config.rootSection
    }

    protected abstract fun init()

    protected abstract fun spawnDefaultConfig()

    override abstract fun save()

    fun isEmpty(): Boolean {
        return configSection.isEmpty()
    }
}