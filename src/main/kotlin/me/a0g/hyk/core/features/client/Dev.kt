package me.a0g.hyk.core.features.client

import me.a0g.hyk.HykX
import me.a0g.hyk.core.TextRenderer
import me.a0g.hyk.core.events.PacketEvent
import me.a0g.hyk.core.handlers.ScoreboardHandler
import me.a0g.hyk.mixins.accessors.AccessorS3BPacketScoreboardObjective
import me.a0g.hyk.utils.HypixelUtil
import me.a0g.hyk.utils.MessageUtil
import me.a0g.hyk.utils.TextUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiControls
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.server.S2FPacketSetSlot
import net.minecraft.network.play.server.S3BPacketScoreboardObjective
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.commons.lang3.StringUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.awt.Color
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

class Dev {

    private fun isDev(): Boolean {
        return HykX.isDev
    }

    @SubscribeEvent
    fun onPacket(event: PacketEvent.ReceiveEvent){

        if(HykX.config.receivePacketLog){
            HykX.logger.info(event.packet)
        }

        if(HykX.config.noAprilFool && HypixelUtil.isMultiplayer()){
            (event.packet as? S3BPacketScoreboardObjective)?.let { packet ->
                if(ScoreboardHandler.cleanSB(packet.func_149337_d()).contains("SKIBLOCK")){
                    (packet as AccessorS3BPacketScoreboardObjective).setObjectiveValue("SKYBLOCK")
                }
            }
        }
    }

    @SubscribeEvent
    fun onPacketSend(event: PacketEvent.SendEvent){
        if(HykX.config.sendPacketLog){
            if(event.packet !is C03PacketPlayer){
                HykX.logger.info(event.packet)
            }
        }
    }

    @SubscribeEvent
    fun onGuiDraw(event: DrawScreenEvent.Post) {
        //guiName
        if(HykX.isDev){
            if(Minecraft.getMinecraft().currentScreen === null){
                TextRenderer("nul",50,50,1.0)
            }else{
                TextRenderer(Minecraft.getMinecraft().currentScreen.toString(),50,50,1.0)
            }
        }
    }

    private var lastCopy = 0L

    @SubscribeEvent
    fun onItemTooltip(e: ItemTooltipEvent) {

       // HykX.logger.info("Tooltip")
        if (e.itemStack == null || e.toolTip == null || e.entityPlayer == null) {
            return
        }

        //Price for each item auction
        if (HykX.isDev) {
           // HykX.logger.info("Tooltip")
            val displayName = e.itemStack.displayName
            if(displayName != null) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_LMENU) && (lastCopy + 200 < System.currentTimeMillis())) { // alt key pressed and right mouse button being released
                    GuiControls.setClipboardString(displayName)
                    lastCopy = System.currentTimeMillis()
                    HykX.logger.info("Copy clipboard")
                }
            }
            val tooltipAdd = EnumChatFormatting.GOLD.toString() + "Hyk Dev copy display name with Ctrl and Alt"
            e.toolTip.add(tooltipAdd)
            if(displayName != null){
                e.toolTip.add(displayName)
            }

        }
    }

}