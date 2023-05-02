package me.a0g.hyk.utils

import com.google.gson.JsonObject
import me.a0g.hyk.core.handlers.APIHandler
import net.minecraft.util.EnumChatFormatting
import kotlin.math.roundToInt

object HypixelApiUtil {

    private val cachePlayerData = HashMap<String, JsonObject?>()


    fun getPlayerData(playerUUID: String,apiKey: String): JsonObject? {
        if (cachePlayerData.containsKey(playerUUID)) {
            //FMLLog.info("from cache ${cachePlayerData[playerUUID]}")
            return cachePlayerData[playerUUID]
        }
        val url = "https://api.hypixel.net/player?key=$apiKey&uuid=$playerUUID"
        val apiData = APIHandler.getResponse(url)
        val playerData = apiData.get("player").asJsonObject
        if (apiData.get("success").asBoolean) {
            cachePlayerData[playerUUID] = playerData
            return playerData
        }
        return null
    }


    fun getName(player: JsonObject): String {
        val name = player["displayname"].asString
        lateinit var rank : String
        var plusColor = EnumChatFormatting.RED //rankPlusColor
        if (player.has("prefix")) {
            rank = player["prefix"].asString
        }
        else if (player.has("rank") && player["rank"].asString !== "NORMAL") {
            if (player["rank"].asString == "HELPER") rank =
                EnumChatFormatting.DARK_AQUA.toString() + "[HELPER "
            else if (player["rank"].asString == "MODERATOR") rank =
                EnumChatFormatting.DARK_GREEN.toString() + "[MODERATOR]"
            else if (player["rank"].asString == "YOUTUBER") rank =
                EnumChatFormatting.RED.toString() + "[§rYOUTUBER§c]"
            else if (player["rank"].asString == "ADMIN") rank =
                EnumChatFormatting.RED.toString() + "[ADMIN]"
        }
        else if (player.has("newPackageRank")) {
            if (player["newPackageRank"].asString == "MVP_PLUS") {
                if (player.has("rankPlusColor")) {
                    plusColor = EnumChatFormatting.getValueByName(player["rankPlusColor"].asString)
                }
                rank =
                    if (player.has("monthlyPackageRank") && player["monthlyPackageRank"].asString == "SUPERSTAR") "§6[MVP$plusColor++§6]" else "§b[MVP$plusColor+§b]"
            }
            else if (player["newPackageRank"].asString == "MVP") rank =
                EnumChatFormatting.AQUA.toString() + "[MVP]" else if (player["newPackageRank"].asString == "VIP_PLUS") rank =
                EnumChatFormatting.GREEN.toString() + "[VIP§e+§a]" else if (player["newPackageRank"].asString == "VIP") rank =
                EnumChatFormatting.GREEN.toString() + "[VIP]" else rank = ""
        }
        if (rank.isEmpty()) {
            return "§7$name§r"
        }
        return "$rank $name§r"
    }

    fun getSwLvl(player: JsonObject): String {
        if (player.has("stats")) {
            val stats = player["stats"].asJsonObject
            if (stats.has("SkyWars")) {
                val sw = stats["SkyWars"].asJsonObject
                if (sw.has("skywars_experience")) {
                    val xp = sw["skywars_experience"].asInt
                    val xps = intArrayOf(0, 20, 70, 150, 250, 500, 1000, 2000, 3500, 6000, 10000, 15000)
                    if (xp >= 15000) {
                        val lvl = ((xp - 15000) / 10000 + 12).toFloat().roundToInt()
                        var lvlFinale = lvl.toString()
                        var lvlColor : String = ""
                        if (lvl < 15) {
                            lvlFinale = EnumChatFormatting.GOLD.toString() + "" + lvl
                        } else if (lvl < 20) {
                            lvlFinale = EnumChatFormatting.AQUA.toString() + "" + lvl
                        } else if (lvl < 25) {
                            lvlFinale = EnumChatFormatting.DARK_GREEN.toString() + "" + lvl
                        } else if (lvl < 30) {
                            lvlFinale = EnumChatFormatting.DARK_AQUA.toString() + "" + lvl
                        } else if (lvl < 35) {
                            lvlFinale = EnumChatFormatting.DARK_RED.toString() + "" + lvl
                        } else if (lvl < 40) {
                            lvlFinale = EnumChatFormatting.LIGHT_PURPLE.toString() + "" + lvl
                        } else if (lvl < 45) {
                            lvlFinale = EnumChatFormatting.DARK_BLUE.toString() + "" + lvl
                        } else if (lvl < 50) {
                            lvlFinale = EnumChatFormatting.DARK_PURPLE.toString() + "" + lvl
                        } else {
                            for (i in lvlFinale.indices) {
                                if (lvlFinale.length == 2) {
                                    if (i == 0) lvlColor += EnumChatFormatting.GOLD.toString() + "" + lvlFinale[i] + ""
                                    if (i == 1) lvlColor += EnumChatFormatting.YELLOW.toString() + "" + lvlFinale[i] + ""
                                } else if (lvlFinale.length == 3) {
                                    if (i == 0) lvlColor += EnumChatFormatting.GOLD.toString() + "" + lvlFinale[i] + ""
                                    if (i == 1) lvlColor += EnumChatFormatting.YELLOW.toString() + "" + lvlFinale[i] + ""
                                    if (i == 2) lvlColor += EnumChatFormatting.GREEN.toString() + "" + lvlFinale[i] + ""
                                }
                            }
                            return lvlColor
                        }
                        return lvlFinale
                    } else {
                        for (i in xps.indices) {
                            if (xp < xps[i]) {
                                val lvl = (i + (xp - xps[i - 1]) / (xps[i] - xps[i - 1])).toFloat().roundToInt()
                                var lvlFinale = ""
                                if (lvl < 5) {
                                    lvlFinale = EnumChatFormatting.DARK_GRAY.toString() + "" + lvl
                                } else if (lvl < 10) {
                                    lvlFinale = EnumChatFormatting.WHITE.toString() + "" + lvl
                                } else if (lvl < 15) {
                                    lvlFinale = EnumChatFormatting.GOLD.toString() + "" + lvl
                                }
                                return lvlFinale
                            }
                        }
                    }
                }
            }
        }
        return "0"
    }

    fun getSwStats(player: JsonObject): String? {
        if (player.has("stats")) {
            val stats = player["stats"].asJsonObject
            if (stats.has("SkyWars")) {
                val skyWars = stats["SkyWars"].asJsonObject
                if (skyWars.has("kills") && skyWars.has("wins") && skyWars.has("deaths") && skyWars.has("losses")) {
                    val kills: Int = skyWars["kills"].asInt
                    val deaths: Double = skyWars["deaths"].asDouble
                    val wins: Int = skyWars["wins"].asInt
                    val losses: Double = skyWars["losses"].asDouble

                    val wl = (wins / losses * 100.0).roundToInt() / 100.0
                    val kd = (kills / deaths * 100.0).roundToInt() / 100.0
                    //r
                    return "§7Kills:§c$kills§7 Wins:§a$wins§7 KD:§c$kd§7 WL:§e$wl"
                }
            }
        }
        return "low"
    }


}