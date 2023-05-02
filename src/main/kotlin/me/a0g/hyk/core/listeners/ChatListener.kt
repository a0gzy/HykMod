package me.a0g.hyk.core.listeners

import gg.essential.api.utils.Multithreading
import gg.essential.universal.UResolution
import me.a0g.hyk.HykX
import me.a0g.hyk.core.features.hypixel.BuildBattleHelper
import me.a0g.hyk.mixins.accessors.AccessorGuiNewChat
import me.a0g.hyk.utils.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ChatLine
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiControls
import net.minecraft.client.gui.GuiNewChat
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.MathHelper
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.util.*
import java.util.regex.Pattern
import kotlin.concurrent.thread

class ChatListener {

    private val BBH_PATTERN = Pattern.compile("The theme is (.+)")


    private val config = HykX.config

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        val unformattedText = event.message.unformattedText
        val cleanText = TextUtil.stripColor(unformattedText)

        //BuildBattle
        if(config.bbh && HypixelUtil.checkForGame("GUESS THE BUILD")){

            if(cleanText != null && cleanText.contains("Builder: ")) {
                HykX.bbh.correct = false
            }

            if(cleanText != null && cleanText.contains(" (Correct Guess)")) {
                HykX.bbh.correct = true
            }

            if (event.type.toInt() == 2) {
                val clearedTheme= TextUtil.stripColor(unformattedText)
                val matcher = BBH_PATTERN.matcher(clearedTheme)
                if (matcher.matches()) {
                    val theme = matcher.group(1).lowercase(Locale.getDefault())
                    if(HykX.bbh.theme != theme && !HykX.bbh.correct) {
                        HykX.bbh.updateTheme(theme)
                    }
                }
            }
        }

        //Your new API key is
        if (cleanText != null) {
            if (cleanText.startsWith("Your new API key is ")) {
                //String apiValue = event.message.getChatStyle().getChatClickEvent().getValue();
                val apiKey = cleanText.substring("Your new API key is ".length)
                //  String apikey[] = unformattedText.split("Your new API key is ");
                // FMLLog.info(apikey[1] + "");
                //  String key = apikey[1].replaceAll(" ","");
                config.apiKey = apiKey
//                MessageUtil.sendMessage(EnumChatFormatting.DARK_AQUA.toString() + "API key saved")
                config.markDirty()
                config.writeData()
            }
        }

    }

}