package me.a0g.hyk.commands

import com.google.gson.JsonObject
import gg.essential.api.utils.Multithreading
import me.a0g.hyk.HykX
import me.a0g.hyk.core.handlers.APIHandler
import me.a0g.hyk.utils.HypixelApiUtil
import me.a0g.hyk.utils.MessageUtil
import me.a0g.hyk.utils.TabCompletionUtil
import me.a0g.hyk.utils.TextUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos

class GetSw : BaseCmd("sws") {

    override fun getCommandUsage(player: EntityPlayerSP): String = "/${this.commandName} [player]"

    override fun processCommand(player: EntityPlayerSP, args: Array<String>) {
        if (args.size > 1) return MessageUtil.sendMessage(getCommandUsage(player))
        var name = Minecraft.getMinecraft().thePlayer.name
        if (args.isNotEmpty()) {
            name = args[0]
        }
        if (HykX.config.apiKey.isEmpty()) {
            MessageUtil.sendMessage("§cNo API key settled")
            return
        }
        val apiKey = HykX.config.apiKey
        if (!TextUtil.isUsername(name)) return
        Multithreading.runAsync {
            val uuid = try {
                APIHandler.getUUID(name)
            } catch (e: Exception) {
                MessageUtil.sendMessage("§cFailed to get UUID, nicked?")
                return@runAsync
            }
            //FMLLog.info(uuid)
            val playerData: JsonObject? = try {
                HypixelApiUtil.getPlayerData(uuid, apiKey)
            } catch (e: Exception) {
                MessageUtil.sendMessage("§cFailed to get Hypixel Data")
                return@runAsync
            }
            //FMLLog.info(playerData.toString())
            // | VIP+ Man: 16✫ Kills:2511 Wins:251 KD:1.92 WL:0.32
            val hypixelNick: String? = playerData?.let { HypixelApiUtil.getName(it) }
            val lvl: String? = playerData?.let { HypixelApiUtil.getSwLvl(it) }
            val stats: String? = playerData?.let { HypixelApiUtil.getSwStats(it) }

            val message = "$hypixelNick: $lvl✫§r $stats"

            MessageUtil.sendDataMessage(message)
        }
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender?,
        args: Array<String>?,
        pos: BlockPos?
    ): List<String?>? {
        return if (args!!.size == 1)
            TabCompletionUtil.getListOfStringsMatchingLastWord(
                args, TabCompletionUtil.getTabUsernames()
        ) else null
    }

}