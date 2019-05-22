package io.wetabq.quickshop.config

import io.wetabq.quickshop.QuickShop

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class LanguageConfig : QuickShopConfig("language") {
    var languageConfig =  hashMapOf(
            "&cYour money is not enough to buy,you still need \${} to buy" to "",
            "&cThe shop is out of stock and cannot be sold" to "",
            "&aSuccessfully purchased &6{}*{}" to "",
            "&aPlayer &e{} &asuccessfully purchased &6{}*{} &aand got &e{}$" to "",
            "&cYour inventory is full and cannot be purchased" to "",
            "&cThe owner money is not enough to buy" to "",
            "&cYour inventory is not enough to sell" to "",
            "&cThe owner's chest is full!" to "",
            "&aSuccessfully sold &6{}*{}" to "",
            "&aPlayer &e{} &asuccessfully sold &6{}*{} &aand reduce &e{}$" to "",
            "&cPlease put a sign around your chest." to "",
            "Please enter the &e&lprice &r&ato set up your shop!" to "",
            "&aType: {}" to "",
            "&eItem: {}" to "",
            "&c&lPrice: {}$ /count" to "",
            "&c[&l&eQuick&6Shop&r&c]" to "",
            "Successfully created the shop" to "",
            "&d+-------INFO-------+\n&d| &aOwner: &e{}\n&d| &aItem: &e{}\n&d| &aPrice: &e{}\$ /count\n&d| &aThis shop is &b{} &aitems\n&d+------------------+" to "",
            "Please enter the &l&ecount &r&ayou need to &d&l{}" to "",
            "&6----QuickShop Command----" to "",
            "&b/qs help(h) - View help" to "",
            "&b/qs buy(b) - Set the current clicked shop as the buy type" to "",
            "&b/qs sell(s) - Set the current clicked shop as the sell type" to "",
            "&b/qs price(p) <price: Int> - Set the current clicked shop's price" to "",
            "&b/qs unlimited(ul) - Set the current clicked shop as unlimited" to "",
            "&b/qs version(v) - View QuickShop version" to "",
            "&cPlease click on your own shop first." to "",
            "&cThat is not your own shop." to "",
            "Successfully changed the shop type" to "",
            "Successfully changed the shop price" to "",
            "Successfully changed the shop unlimited[{}]" to "",
            "&cPlease enter the correct number" to ""
    )

    init {
        init()
    }

    override fun init() {
        if (!isEmpty()) {
            try {
                languageConfig = configSection.get("language") as HashMap<String,String>
            } catch (e: Exception) {
                QuickShop.instance.logger.warning(e.message)
                QuickShop.instance.logger.error("Language Config(language.yml) while loading config")
            }

        } else {
            spawnDefaultConfig()
        }
    }

    override fun spawnDefaultConfig() {
        if (isEmpty()) {
            configSection["language"] = languageConfig
        }
        save()
        init()
    }

    override fun save() {
        if (!isEmpty()) {
            //NOT NEED SAVE
        } else {
            spawnDefaultConfig()
        }
    }
}