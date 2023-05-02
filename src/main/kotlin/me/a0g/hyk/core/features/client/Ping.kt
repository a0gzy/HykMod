package me.a0g.hyk.core.features.client

import me.a0g.hyk.HykX
import me.a0g.hyk.core.events.PacketEvent
import net.minecraft.client.Minecraft
import net.minecraft.network.play.client.C16PacketClientStatus
import net.minecraft.network.play.server.S01PacketJoinGame
import net.minecraft.network.play.server.S37PacketStatistics
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.abs

object Ping {

    var lastPingAt = -1L

    var pingCache = -1.0

    fun sendPing() {
        if (lastPingAt > 0) {
            return
        }
        HykX.logger.info("sendPing")
        Minecraft.getMinecraft().thePlayer.sendQueue.networkManager.sendPacket(
            C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS),
            {
                lastPingAt = System.nanoTime()
            }
        )
    }

    @SubscribeEvent
    fun onPacket(event: PacketEvent.ReceiveEvent) {
        if (lastPingAt > 0) {
            when (event.packet) {
                is S01PacketJoinGame -> {
                    lastPingAt = -1L
                }

                is S37PacketStatistics -> {
                    val diff = (abs(System.nanoTime() - lastPingAt) / 1_000_000.0)
                    lastPingAt *= -1
                    pingCache = diff
                }
            }
        }
    }

}