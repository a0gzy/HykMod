package me.a0g.hyk.core.features.client

import gg.essential.universal.UResolution
import me.a0g.hyk.HykX
import me.a0g.hyk.mixins.accessors.AccessorGuiNewChat
import me.a0g.hyk.utils.MessageUtil
import me.a0g.hyk.utils.ReflectionHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ChatLine
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiControls
import net.minecraft.client.gui.GuiNewChat
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.MathHelper
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

class ChatCopy {

    @SubscribeEvent
    fun onClickOnChat(e: GuiScreenEvent.MouseInputEvent.Pre) {
        if (Mouse.getEventButton() < 0) {
            // no button press, just mouse-hover
            return
        }
        if (e.gui is GuiChat && HykX.config.chatCopy) {
            if (!Mouse.getEventButtonState() && Mouse.getEventButton() == 1 && Keyboard.isKeyDown(Keyboard.KEY_LMENU)) { // alt key pressed and right mouse button being released
                //val chatComponent = Minecraft.getMinecraft().ingameGUI.chatGUI.getChatComponent(Mouse.getX(), Mouse.getY())
                val chatLine = Minecraft.getMinecraft().ingameGUI.chatGUI.getChatLine(Mouse.getX(), Mouse.getY())
                val component = chatLine?.chatComponent
                if (component != null) {
                    val copyWithFormatting = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
                    var chatData: String
                    if (copyWithFormatting) {
                        chatData = component.formattedText.replace("§".toRegex(), "&")
                    } else {
                        chatData = EnumChatFormatting.getTextWithoutFormattingCodes(component.unformattedText)
                        if (chatData.startsWith(": ")) {
                            chatData = chatData.substring(2)
                        }
                    }
                    GuiControls.setClipboardString(chatData)
                    MessageUtil.sendMessage(EnumChatFormatting.GREEN.toString() + "Copied chat component to clipboard.")
                }
            }
        }
    }

    fun GuiNewChat.getChatLine(mouseX: Int, mouseY: Int): ChatLine? {
        if (chatOpen && this is AccessorGuiNewChat) {
            val scaleFactor = UResolution.scaleFactor
            val extraOffset =
                if (ReflectionHelper.getFieldFor("club.sk1er.patcher.config.PatcherConfig", "chatPosition")
                        ?.getBoolean(null) == true
                ) 12 else 0
            val x = MathHelper.floor_float((mouseX / scaleFactor - 3).toFloat() / chatScale)
            val y = MathHelper.floor_float((mouseY / scaleFactor - 27 - extraOffset).toFloat() / chatScale)

            if (x >= 0 && y >= 0) {
                val l = this.lineCount.coerceAtMost(this.drawnChatLines.size)
                if (x <= MathHelper.floor_float(this.chatWidth.toFloat() / this.chatScale) && y < Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * l + l) {
                    val lineNum = y / Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + this.scrollPos
                    if (lineNum >= 0 && lineNum < this.drawnChatLines.size) {
                        return drawnChatLines[lineNum]
                    }
                }
            }
        }
        return null
    }

}