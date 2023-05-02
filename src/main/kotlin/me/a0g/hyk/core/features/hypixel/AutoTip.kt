package me.a0g.hyk.core.features.hypixel

import me.a0g.hyk.HykX
import me.a0g.hyk.utils.HypixelUtil
import net.minecraft.client.Minecraft
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class AutoTip {


    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase !== TickEvent.Phase.START) return
        if(HykX.tickListener.totalTicks.toInt() % (20 * 60 * 10)!= 0) return  //ticks * seconds * minutes
        if(HypixelUtil.isMultiplayer() && HypixelUtil.isHypixel() && HykX.config.autoTip) {
            val command = "/tipall"
            if (ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().thePlayer, command) != 0) {
                HykX.logger.info("tipall")
                return
            }
            Minecraft.getMinecraft().thePlayer.sendChatMessage(command)
        }
    }

}