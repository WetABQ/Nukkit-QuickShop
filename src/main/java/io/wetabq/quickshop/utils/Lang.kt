package io.wetabq.quickshop.utils

import cn.nukkit.item.Item
import cn.nukkit.utils.TextFormat
import io.wetabq.quickshop.QuickShop

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
object Lang {

    @JvmStatic
    fun getMessage(message: String,useTitle: Boolean): String {
        return getMessage(message, arrayOf<Any>(),useTitle)
    }

    @JvmStatic
    fun getMessage(message: String): String {
        return getMessage(message, arrayOf<Any>(),true)
    }

    @JvmStatic
    fun getMessage(message: String,parameters: Array<*>): String {
        return getMessage(message,parameters,true)
    }

    @JvmStatic
    fun getMessage(message: String,parameters: Array<*>,useTitle: Boolean): String {
        var m = message
        val languageConfig = QuickShop.instance.languageConfig.languageConfig
        if (languageConfig.containsKey(m) && !languageConfig[m].equals("")) {
            m = languageConfig[m]!!
        }
        for (parameter in parameters) {
            m = m.replaceFirst("{}",parameter.toString())
        }
        if (useTitle) {
            m = QuickShop.TITLE + m
        }
        return TextFormat.colorize(m)
    }

    @JvmStatic
    fun getItemName(item: Item): String {
        val itemNameConfig = QuickShop.instance.itemNameConfig.itemNameConfig
        if (!itemNameConfig.containsKey(item.id.toString())) {
            if (!itemNameConfig.containsKey("${item.id}:${item.damage}")) {
                return item.name
            } else {
                return itemNameConfig["${item.id}:${item.damage}"]!!
            }
        } else {
            return itemNameConfig[item.id.toString()]!!
        }
    }

}