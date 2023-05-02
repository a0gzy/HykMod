package me.a0g.hyk.utils

import me.a0g.hyk.HykX
import me.a0g.hyk.core.handlers.ScoreboardHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.Slot
import java.util.*
import kotlin.math.roundToInt

object HypixelUtil {

    fun isMultiplayer(): Boolean {
        val mc = Minecraft.getMinecraft()
        if (mc?.theWorld != null) {
            if(HykX.isDev) return true
            if(!mc.isSingleplayer) return true
        }
        return false
    }

    /**
     * Check for hypixel
     *
     * @return is on Hypixel : Boolean
     */
    fun isHypixel(): Boolean {
        if(Minecraft.getMinecraft().currentServerData?.serverIP?.contains("hypixel",true) == true) {
            return true
        }
        return false
    }

    /**
     * Check for current game
     *
     * @param miniGame
     * @return is in miniGame : Boolean
     */
    fun checkForGame(miniGame: String?): Boolean {
        if (isMultiplayer()) {
            val scoreboardObj = Minecraft.getMinecraft().theWorld.scoreboard.getObjectiveInDisplaySlot(1)
            if (scoreboardObj != null) {
                val scObjName = ScoreboardHandler.cleanSB(scoreboardObj.displayName)
                if (scObjName.contains(miniGame!!)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Checks if in the SkyBlock
     *
     * @return is on SkyBlock : Boolean
     */
    fun checkForSkyBlock(): Boolean {
        if (isMultiplayer()) {
            if(HykX.isDev) return true

            val scoreboardObj = Minecraft.getMinecraft().theWorld.scoreboard.getObjectiveInDisplaySlot(1)
            if (scoreboardObj != null) {
                TextUtil.stripColor(scoreboardObj.displayName)?.let {
                    if(it.contains("SK[YI]BLOCK".toRegex())){
                        return true
                    }
                }
            }
        }
        return false
    }

    fun isGarden():Boolean {
        if(HykX.isDev) return true
        if(checkForSkyBlock() && checkScoreboardString("Copper:")){
            return true
        }
        return false
    }


    /**
     * Updates current Discord RPC
     *
     */
    fun setRPCGame() { //rework
        val mc = Minecraft.getMinecraft()
        if (mc?.theWorld != null && !mc.isSingleplayer && mc.currentServerData != null) {
            HykX.discordRpc.details = mc.thePlayer.name + " -> " + mc.currentServerData.serverIP
            val scoreboardObj = mc.theWorld.scoreboard.getObjectiveInDisplaySlot(1)
            if (scoreboardObj != null) {
                val scObjName = ScoreboardHandler.cleanSB(scoreboardObj.displayName)
                val game = scObjName.lowercase(Locale.getDefault()).replace(" ".toRegex(), "")
                var state = "Playing " + game.uppercase()
                val smallImage: String
                when(game){
                    "skywars" -> smallImage = "skywars"
                    "murdermystery" -> {
                        smallImage = "murdermystery"
                        val mRole = getScoreboardStringWith("Role: ")?.replace("Role: ","")
                        state += " as $mRole"
                    }
                    else -> smallImage = ""
                }

                val map = getScoreboardStringWith("Map: ")
                if(map != null){
                    state += " on " + map.replace("Map: ", "")
                }


                HykX.discordRpc.state = state
                HykX.discordRpc.smallImageKey = smallImage
                HykX.discordRpc.smallImageText = smallImage
            } else {
                HykX.discordRpc.state = "AFK"
                HykX.discordRpc.smallImageKey = ""
                HykX.discordRpc.smallImageText = ""
            }
        } else {
            HykX.discordRpc.details = mc!!.session.username + " -> 1.8.9"
            HykX.discordRpc.state = "Menu"
            HykX.discordRpc.smallImageKey = ""
            HykX.discordRpc.smallImageText = ""
        }
    }


    /**
     * Check for island in scoreboard
     *
     * @return is on your SkyBlock island : Boolean
     */
    fun checkForIsland(): Boolean {
        if (checkScoreboardString("Your Island")) {
            return true
        }
        return false
    }


    /**
     * Check scoreboard string
     *
     * @param text what to find in scoreboard
     * @return does scoreboard have your text : Boolean
     */
    fun checkScoreboardString(text: String?): Boolean {
        if (isMultiplayer()) {
            if(HykX.isDev) return true

            val scoreboard = ScoreboardHandler.getSidebarLines()
            for (s in scoreboard) {
                val sCleaned = ScoreboardHandler.cleanSB(s)
                if (sCleaned.contains(text!!)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Check scoreboard string
     *
     * @param text what to find in scoreboard
     * @return does scoreboard have your text : Boolean
     */
    fun checkScoreboardString(text: Regex): Boolean {
        if (isMultiplayer()) {
            if(HykX.isDev) return true

            val scoreboard = ScoreboardHandler.getSidebarLines()
            for (s in scoreboard) {
                val sCleaned = ScoreboardHandler.cleanSB(s)
                if (sCleaned.contains(text)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Check tab string
     *
     * @param text what to find in tab
     * @return does tab have your text : Boolean
     */
    fun checkTabString(text: String?): Boolean {
        if (isMultiplayer()) {
            try {
                val tabList = MinecraftUtil.getTabList()
                for (tab in tabList) {
                    val stringCleaned = TextUtil.stripColor(tab)
                    if (stringCleaned != null && stringCleaned.contains(text!!)) {
                        return true
                    }
                }
            }catch (e : Error){
                HykX.logger.error(e)
            }


        }
        return false
    }

    /**
     * Gets tab string with input text
     *
     * @param text what to find in tab
     * @return string with given text : String?
     */
    fun getTabStringWith(text: String?): String? {
        if (isMultiplayer()) {
            try {
                val tabList = MinecraftUtil.getTabList()
                for (tab in tabList) {
                    val stringCleaned = TextUtil.stripColor(tab)
                    if (stringCleaned != null && stringCleaned.contains(text!!)) {
                        return stringCleaned
                    }
                }
            }catch (e : Error){
                HykX.logger.error(e)
            }
        }
        return null
    }

    /**
     * Get scoreboard string with text
     *
     * @param text what to find in scoreboard
     * @return scoreboard string with given text in it : String?
     */
    fun getScoreboardStringWith(text: Regex): String? {
        if (isMultiplayer()) {
            val scoreboard = ScoreboardHandler.getSidebarLines()
            for (s in scoreboard) {
                val sCleaned = ScoreboardHandler.cleanSB(s)
                if (sCleaned.contains(text)) {
                    return sCleaned
                }
            }
        }
        return null
    }


    /**
     * Get scoreboard string with text
     *
     * @param text what to find in scoreboard
     * @return scoreboard string with given text in it : String?
     */
    fun getScoreboardStringWith(text: String?): String? {
        if (isMultiplayer()) {
            val scoreboard = ScoreboardHandler.getSidebarLines()
            for (s in scoreboard) {
                val sCleaned = ScoreboardHandler.cleanSB(s)
                if (sCleaned.contains(text!!)) {
                    return sCleaned
                }
            }
        }
        return null
    }

    /**
     * Check for Dungeons
     *
     * @return is you in Dungeons : Boolean
     */
    fun checkForDungeons(): Boolean {
        if (checkForSkyBlock()) {
            if(HykX.isDev) return true

            if(checkScoreboardString("The Catacombs")){
                return true
            }
        }
        return false
    }



}