package me.a0g.hyk.core.listeners

import me.a0g.hyk.HykX
import me.a0g.hyk.core.TextRenderer
import me.a0g.hyk.core.components.PositionDouble
import me.a0g.hyk.core.handlers.RenderString
import me.a0g.hyk.core.handlers.RenderText
import me.a0g.hyk.gui.Gui
import me.a0g.hyk.utils.HypixelUtil
import me.a0g.hyk.utils.RenderUtil
import me.a0g.hyk.utils.TextUtil
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.settings.KeyBinding
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class RenderListener {

    private val mc = Minecraft.getMinecraft()
    private val config = HykX.config

    @SubscribeEvent
    fun renderGameOverlayEvent(event: RenderGameOverlayEvent.Post) {
        if (event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE
            || event.type == RenderGameOverlayEvent.ElementType.JUMPBAR
        ) {
            if (mc.currentScreen is Gui) return

            renderMainOverlay()

            renderOtherOverlay()

        }
    }

    private fun renderOtherOverlay() {

        if(config.fryctDisplay){
            val fryctPos = PositionDouble(config.fryctPos)
            RenderUtil.drawTextWithPanel("${config.fryctText} ${getFryctTimer()}",fryctPos.x,fryctPos.y,fryctPos.scale)
        }
        if (config.autoSprintEnabled) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.keyCode, config.autoSprintEnabled)
        }

    }

    private fun getFryctTimer(): String {
        try {
            val come = LocalDateTime.of(2023,Month.DECEMBER,2,17,0).atZone(ZoneId.systemDefault()).toEpochSecond()
            val localTime = LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toEpochSecond()
            return TextUtil.getTimeBetween(localTime / 1.0, come / 1.0)
        }catch (_: Exception){
        }
        return ""
    }

    private fun renderMainOverlay() {
        if (config.autoSprintEnabled) {
            RenderText(
                config.sprintText.replace("&".toRegex(), "§"),
                PositionDouble(config.sprintPos),
                config.sprintColor
            )
        }
        if (config.timeDisplay) {
            RenderText(
                getRenderTime(),
                PositionDouble(config.timePos),
                config.timeDisplayColor
            )
        }
        if (config.fpsDisplay) {
            RenderText(
                "FPS: " + Minecraft.getDebugFPS(),
                PositionDouble(config.fpsPos),
                config.fpsDisplayColor
            )
        }
        if (config.cpsDisplay) {
            RenderText(
                "CPS: " + getPlayerLeftCPS(),
                PositionDouble(config.cpsPos),
                config.cpsDisplayColor
            )
        }
        if (config.armorHud) {
            renderArmor(PositionDouble(config.armorPos))
        }
    }

    private fun renderArmor(pos: PositionDouble) {
        GlStateManager.pushMatrix()
        GlStateManager.scale(pos.scale, pos.scale, pos.scale)
        for (item in mc.thePlayer.inventory.armorInventory.indices) {
            val armor = mc.thePlayer.inventory.armorInventory[item]
            if (armor != null) {
                val offset = -16 * item + 48
                RenderUtil.renderArmor(armor, pos.x / pos.scale, pos.y / pos.scale + offset)
            }
        }
        GlStateManager.popMatrix()
    }

    private fun getRenderTime(): String {
        val d = Date()
        val format1 = SimpleDateFormat("HH:mm")
        return format1.format(d)
    }

    private val leftClicks: MutableList<Long> = ArrayList()

    private var leftWasPressed = false


    private fun getPlayerLeftCPS(): Int {
        val pressed = Mouse.isButtonDown(0)
        if (pressed != leftWasPressed) {
            leftWasPressed = pressed
            val leftLastPressed = System.currentTimeMillis()
            if (pressed) {
                leftClicks.add(leftLastPressed)
            }
        }
        leftClicks.removeIf { looong: Long -> looong + 1000 < System.currentTimeMillis() }
        return leftClicks.size
    }

}