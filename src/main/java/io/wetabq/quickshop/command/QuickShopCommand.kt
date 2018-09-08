package io.wetabq.quickshop.command

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.utils.TextFormat
import io.wetabq.quickshop.QuickShop
import io.wetabq.quickshop.listener.InteractionShopListener
import io.wetabq.quickshop.shop.ShopType
import io.wetabq.quickshop.utils.Lang
import java.util.HashMap

/**
 * QuickShop
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
class QuickShopCommand : Command("qs") {

    init {
        this.setDescription("QuickShop Command")
        this.aliases = arrayOf("shop","qshop","quickshop")
        this.usage = "/qs <subcommand> [args]"
        this.setCommandParameters(object : HashMap<String, Array<CommandParameter>>() {
            init {
                put("1arg", arrayOf(CommandParameter("help(h)", false, arrayOf("help", "h"))))
                put("2arg", arrayOf(CommandParameter("buy(b)", false, arrayOf("buy", "b"))))
                put("3arg", arrayOf(CommandParameter("sell(s)", false, arrayOf("sell", "s"))))
                put("4arg", arrayOf(CommandParameter("price(p)", false, arrayOf("price", "p")),
                        CommandParameter("price", CommandParameter.ARG_TYPE_INT, false)))
                put("5arg", arrayOf(CommandParameter("unlimited(ul)", false, arrayOf("unlimited", "ul"))))
                put("6arg", arrayOf(CommandParameter("version(v)", false, arrayOf("version", "v"))))
            }
        })
    }


    override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sendHelp(sender)
            return true
        }
        when (args[0]) {
            "help","h" -> {
                sendHelp(sender)
            }
            "buy","b" -> {
                if (sender is Player) {
                    if (InteractionShopListener.interactShop.containsKey(sender.name)) {
                        val shop = InteractionShopListener.interactShop[sender.name]!!.second
                        if (shop.shopData.owner.equals(sender.name)) {
                            shop.shopData.type = ShopType.BUY
                            shop.getShopSign()?.setText(Lang.getMessage("&c[&l&eQuick&6Shop&r&c]",false),
                                    Lang.getMessage("&aType: {}", arrayOf("BUY"),false),
                                    shop.getShopSign()!!.text[2],
                                    shop.getShopSign()!!.text[3]
                            )
                            QuickShop.instance.shopConfig.shopData["${shop.shopData.signX}:${shop.shopData.chestY}:${shop.shopData.signZ}:${shop.shopData.level}"] = shop.shopData
                            QuickShop.instance.shopConfig.save()
                            sender.sendMessage(Lang.getMessage("Successfully changed the shop type"))
                        } else {
                            sender.sendMessage(Lang.getMessage("&cThat is not your own shop."))
                        }
                    } else {
                        sender.sendMessage(Lang.getMessage("&cPlease click on your own shop first."))
                    }
                }
            }
            "sell","s" -> {
                if (sender is Player) {
                    if (InteractionShopListener.interactShop.containsKey(sender.name)) {
                        val shop = InteractionShopListener.interactShop[sender.name]!!.second
                        if (shop.shopData.owner.equals(sender.name)) {
                            shop.shopData.type = ShopType.SELL
                            shop.getShopSign()?.setText(Lang.getMessage("&c[&l&eQuick&6Shop&r&c]",false),
                                    Lang.getMessage("&aType: {}", arrayOf("SELL"),false),
                                    shop.getShopSign()!!.text[2],
                                    shop.getShopSign()!!.text[3]
                                    )
                            QuickShop.instance.shopConfig.shopData["${shop.shopData.signX}:${shop.shopData.chestY}:${shop.shopData.signZ}:${shop.shopData.level}"] = shop.shopData
                            QuickShop.instance.shopConfig.save()
                            sender.sendMessage(Lang.getMessage("Successfully changed the shop type"))
                        } else {
                            sender.sendMessage(Lang.getMessage("&cThat is not your own shop."))
                        }
                    } else {
                        sender.sendMessage(Lang.getMessage("&cPlease click on your own shop first."))
                    }
                }
            }
            "price","p" -> {
                if (sender is Player) {
                    if (args.size == 2) {
                        if (QuickShop.isInteger(args[1])) {
                            if (InteractionShopListener.interactShop.containsKey(sender.name)) {
                                val shop = InteractionShopListener.interactShop[sender.name]!!.second
                                if (shop.shopData.owner.equals(sender.name)) {
                                    shop.shopData.price = args[1].toInt()
                                    shop.getShopSign()?.setText(
                                            Lang.getMessage("&c[&l&eQuick&6Shop&r&c]",false),
                                            shop.getShopSign()!!.text[1],
                                            shop.getShopSign()!!.text[2],
                                            Lang.getMessage("&c&lPrice: {}$ /count", arrayOf(args[1]),false))
                                    QuickShop.instance.shopConfig.shopData["${shop.shopData.signX}:${shop.shopData.chestY}:${shop.shopData.signZ}:${shop.shopData.level}"] = shop.shopData
                                    QuickShop.instance.shopConfig.save()
                                    sender.sendMessage(Lang.getMessage("Successfully changed the shop price"))
                                } else {
                                    sender.sendMessage(Lang.getMessage("&cThat is not your own shop."))
                                }
                            } else {
                                sender.sendMessage(Lang.getMessage("&cPlease click on your own shop first."))
                            }
                        } else {
                            sendHelp(sender)
                        }
                    } else {
                        sendHelp(sender)
                    }
                }
            }
            "unlimited","ul" -> {
                if (sender is Player) {
                    if (InteractionShopListener.interactShop.containsKey(sender.name)) {
                        val shop = InteractionShopListener.interactShop[sender.name]!!.second
                        if (shop.shopData.owner.equals(sender.name)) {
                            shop.shopData.unlimited = !shop.shopData.unlimited
                            QuickShop.instance.shopConfig.shopData["${shop.shopData.signX}:${shop.shopData.chestY}:${shop.shopData.signZ}:${shop.shopData.level}"] = shop.shopData
                            QuickShop.instance.shopConfig.save()
                            sender.sendMessage(Lang.getMessage("Successfully changed the shop unlimited[{}]", arrayOf(shop.shopData.unlimited)))
                        } else {
                            sender.sendMessage(Lang.getMessage("&cThat is not your own shop."))
                        }
                    } else {
                        sender.sendMessage(Lang.getMessage("&cPlease click on your own shop first."))
                    }
                }
            }
            "version","v" -> {
                sender.sendMessage(TextFormat.colorize(QuickShop.TITLE+"&l&eQuick&6Shop &r&c- &a${QuickShop.VERSION} &dMade by WetABQ\n${QuickShop.TITLE}If you have any questions, please feel free to send us feedback to our email wetabq@gmail.com"))
            }
        }
        return true
    }

    private fun sendHelp(sender: CommandSender) {
        sender.sendMessage(Lang.getMessage("&6----QuickShop Command----"))
        sender.sendMessage(Lang.getMessage("&b/qs help(h) - View help"))
        sender.sendMessage(Lang.getMessage("&b/qs buy(b) - Set the current clicked shop as the buy type"))
        sender.sendMessage(Lang.getMessage("&b/qs sell(s) - Set the current clicked shop as the sell type"))
        sender.sendMessage(Lang.getMessage("&b/qs price(p) <price: Int> - Set the current clicked shop's price"))
        sender.sendMessage(Lang.getMessage("&b/qs unlimited(ul) - Set the current clicked shop as unlimited"))
        sender.sendMessage(Lang.getMessage("&b/qs version(v) - View QuickShop version"))
    }

}