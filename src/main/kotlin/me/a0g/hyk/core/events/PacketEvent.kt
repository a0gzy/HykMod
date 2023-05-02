package me.a0g.hyk.core.events

import net.minecraft.network.Packet
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
open class PacketEvent(val packet: Packet<*>): HykEvent() {

    class ReceiveEvent(packet: Packet<*>) : PacketEvent(packet) {
    }

    class SendEvent(packet: Packet<*>) : PacketEvent(packet)
}