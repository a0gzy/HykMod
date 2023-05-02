package me.a0g.hyk.core.listeners

import me.a0g.hyk.HykX
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class TickListener {

    var rpcState = false
    val anchor = Any()
    var totalTicks: Long = 0

    @SubscribeEvent
    fun ticker(event: ClientTickEvent) {
        if (event.phase == TickEvent.Phase.START) {
            synchronized(anchor) { totalTicks++ }
            if (Minecraft.getMinecraft() != null) {
                if (HykX.config.rpc) {
                    try {
                        if (!HykX.discordRpc.isRpcActive() && !rpcState) {
                            HykX.discordRpc.startRpc()
                            rpcState = true
                        }
                    } catch (ex: Throwable) {
                        FMLLog.info("RPC aren't on")
                    }
                } else {
                    try {
                        if (HykX.discordRpc.isRpcActive() && rpcState) {
                            HykX.discordRpc.stopRpc()
                            rpcState = false
                        }
                    } catch (ex: Throwable) {
                        FMLLog.info("RPC isn't off")
                    }
                }
            }
        }
    }

}