package me.a0g.hyk.commands

import me.a0g.hyk.HykX
import me.a0g.hyk.utils.MessageUtil.sendMessage
import me.a0g.hyk.utils.MinecraftUtil.getItemLore
import me.a0g.hyk.utils.TabCompletionUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.FMLLog

class SlotNbt : BaseCmd("slotnbt") {


    override fun processCommand(player: EntityPlayerSP, args: Array<String>) {
        if (args.isEmpty()) {
            val pl: EntityPlayer = Minecraft.getMinecraft().thePlayer
            if (pl.heldItem != null) {
                val stack = pl.heldItem
                HykX.logger.info(stack)
                sendMessage(stack.tagCompound?.toString() ?: "nothing")
            }
        } else if (args.size == 1 && args[0].equals("test", ignoreCase = true)) {
            val pl: EntityPlayer = Minecraft.getMinecraft().thePlayer
            if (pl.heldItem != null) {
                val stack = pl.heldItem

                //item lore
                val tocompact = pl.heldItem.tagCompound.getCompoundTag("display").getTag("Lore").toString()
                val lores = getItemLore(stack)
                for (lore in lores) {
                    FMLLog.info(lore)
                }
                FMLLog.info(lores.toString())
                sendMessage(tocompact)
            }
        } else if (args.size == 1 && args[0].equals("extra", ignoreCase = true)) {
            val pl: EntityPlayer = Minecraft.getMinecraft().thePlayer
            if (pl.heldItem != null) {
                val stack = pl.heldItem

                //item lore
                val tocompact = stack.tagCompound.getCompoundTag("ExtraAttributes").toString()
                sendMessage(tocompact)
            }
        } else {
            sendMessage( getCommandUsage(player), false)
        }
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender?,
        args: Array<String>?,
        pos: BlockPos?
    ): List<String?>? {
        return if (args!!.size == 1)
        getPossibleCompilation(listOf("test","extra"),args) else null
    }


}