package me.a0g.hyk.commands

import gg.essential.api.utils.Multithreading
import me.a0g.hyk.HykX
import me.a0g.hyk.core.features.client.Ping
import me.a0g.hyk.core.features.client.WaypointPath
import me.a0g.hyk.utils.MessageUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.command.ICommandSender
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos

class HykCmd: BaseCmd("hyk")  {

    private val completionList = mutableListOf("bbh","help","fake","path")
    private val pathCompletionList = mutableListOf("add","remove")


    override fun getCommandUsage(player: EntityPlayerSP): String = "/${this.commandName} [${completionList.toString()}]"

    override fun processCommand(player: EntityPlayerSP, args: Array<String>) {
        var helpMap = mapOf( "/hyk §7[" to "",
            "§ebbh§8, " to "Shows possible guess the build words",
            "§ehelp§8, " to "Show this information",
            "§efake§8, " to "Simulate colorful messages with &[Color Code]\n§b&bLike this",
            "§ping§8, " to "Test ping",
            "§epath§8, " to "Allows you to add waypoint paths.",
            "§7]" to "")

        if(HykX.isDev){
            helpMap = mapOf( "/hyk §7[" to "",
                "§ebbh§8, " to "Shows possible guess the build words",
                "§ehelp§8, " to "Show this information",
                "§efake§8, " to "Simulate colorful messages with &[Color Code]\n§b&bLike this",
                "§epath§8, " to "Allows you to add waypoint paths.",
                "§edev§8, " to "entites - get all entites in 5 blocks radius; pos - player BlockPos.",
                "§7]" to "")
        }

        if(args.isEmpty()) { return MessageUtil.sendMap(helpMap) }
        if(args.size > 0) {
            when (args[0]) {
                "help" -> {
                    MessageUtil.sendMap(helpMap)
                }

                "dev" -> {
                    if (args.size == 1) {
                        HykX.isDev = !HykX.isDev
                        MessageUtil.sendMessage("Dev ${HykX.isDev}")
                    }
                    else if (args.size == 2) {
                        when (args[1]){
                            "entities" -> {
                                val p = Minecraft.getMinecraft().thePlayer
                                val aabb = AxisAlignedBB( p.posX - 4, p.posY - 4,
                                    p.posZ - 4,p.posX + 4,
                                    p.posY + 4,p.posZ + 4)
                                val mobsMap = mutableMapOf<Entity,String>()
                                for (entity in Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(Entity::class.java,aabb)) {
                                    mobsMap[entity] = entity.displayName.unformattedText
                                }
                                HykX.logger.info(mobsMap)
                            }
                            "pos" -> {
                                MessageUtil.sendMessage(Minecraft.getMinecraft().thePlayer.position.toString())
                            }

                        }
                    }
                }

                "bbh" -> {
                    if (args.size == 1) {
                        //MessageUtil.sendClickableList(HykX.inGameBbhWords)
                        MessageUtil.sendClickableList(HykX.bbh.words)
                    }
                    if (args.size == 2 && args[1].equals("all", ignoreCase = true)) {
                        MessageUtil.sendClickableList(HykX.bbhWords)
                    }
                }

                "fake" -> {
                    try {
                        val noFakeArgs = args.copyOfRange(1, args.size)
                        val toFake = noFakeArgs.joinToString(" ").replace("&".toRegex(), "§")
                        MessageUtil.sendMessage(toFake, false)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                "ping" -> {
                    try {

                        if (args.size == 1) {
                            Ping.sendPing()
                            Multithreading.runAsync {

                                Thread.sleep(1000)

                                val ping = Ping.pingCache
                                val lastPing = Ping.lastPingAt
                                if(lastPing < 0) {
                                    MessageUtil.sendMessage("ping: $ping", false)
                                }
                            }
                        }else if (args.size == 2 && args[1].equals("cache", ignoreCase = true)) {
                            MessageUtil.sendMessage("cache ping: ${Ping.pingCache}", false)
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                "path" -> {
                    try {
                        val pathHelp =
                                    "\n/hyk path add [pathId] - adds next waypoint \n" +
                                    "/hyk path add [pathId] [waypointId] adds specific waypoint \n" +
                                    "/hyk path remove - removes all paths \n" +
                                    "/hyk path remove [pathId] - removes specific path\n" +
                                    "/hyk path remove [pathId] [waypointId] - removes specific waypoint"

                        if (args.size == 1) {
                            MessageUtil.sendMessage(pathHelp)
                        } else if (args[1] == "add" && args.size > 2) { // /hyk path add 1 1
                            val pathId = args[2].toInt()
                            var positionId: Int
                            if (args.size > 3) {
                                positionId = args[3].toInt()
                            }
                            else {
                                if (WaypointPath.pathsArray[pathId] == null) {
                                    positionId = 0
                                } else {
                                    positionId = WaypointPath.pathsArray[pathId]!!.keys.maxOf { it } + 1
                                }
                            }


                            val blockPos = Minecraft.getMinecraft().thePlayer.position

                            if (WaypointPath.pathsArray[pathId] == null) {
                                val mutableMap = mutableMapOf(positionId to blockPos)
                                WaypointPath.pathsArray[pathId] = mutableMap
                            } else {
                                WaypointPath.pathsArray[pathId]?.put(positionId, blockPos)
                            }
                            MessageUtil.sendMessage("Path $pathId Waypoint $positionId added at $blockPos") // Path 1 Waypoint 1 11,24,66
                        } else if (args[1] == "remove") { // /hyk arg0:path arg1:remove arg2:1 arg3:1
                            if (args.size == 2) { //remove all
                                WaypointPath.pathsArray.clear()

                                MessageUtil.sendMessage("Paths removed")
                            } else if (args.size == 3) {  //remove path
                                val pathId = args[2].toInt()
                                WaypointPath.pathsArray.remove(pathId)

                                MessageUtil.sendMessage("Path $pathId removed")
                            } else if (args.size == 4) { //remove waypoint
                                val pathId = args[2].toInt()
                                val positionId = args[3].toInt()

                                WaypointPath.pathsArray[pathId]?.remove(positionId)

                                MessageUtil.sendMessage("Path $pathId Waypoint $positionId removed") // Path 1 Waypoint 1 11,24,66
                            }
                        } else {
                            MessageUtil.sendMessage(pathHelp)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                else -> {
                    return MessageUtil.sendMap(helpMap)
                }
            }
        }
        else {
            return MessageUtil.sendMap(helpMap)
        }
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender?,
        args: Array<String>?,
        pos: BlockPos?
    ): List<String?>? {
        return if (args!!.size == 1) getPossibleCompilation(completionList,args)
        else if (args.size == 2 && args[0] == "path") getPossibleCompilation(pathCompletionList,args)
        else if (args.size == 2 && args[0] == "dev") getPossibleCompilation(listOf("entities"),args)
        else null
            //completionList else null
    }
}