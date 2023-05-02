package me.a0g.hyk.utils

import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.minecraftforge.fml.common.FMLLog
import org.apache.commons.lang3.StringUtils
import java.io.IOException

object MessageUtil {

    //§3[§6Hyk§3] §r
    private val MESSAGE_PREFIX =
        EnumChatFormatting.DARK_AQUA.toString() + "[" + EnumChatFormatting.GOLD + "HYK" + EnumChatFormatting.DARK_AQUA + "] " + EnumChatFormatting.RESET

    fun sendMessage(text: String, prefix: Boolean) {
        val message = ChatComponentText((if (prefix) MESSAGE_PREFIX else "") + text)
        Minecraft.getMinecraft().thePlayer.addChatMessage(message)
    }

    fun sendMessage(text: String) {
        sendMessage(text, true)
    }

    /**
     * Sends messages with hover text.
     * Example: [HYK] /hyk §ebbh§8, §efake§8, §ehelp§8,
     *
     * @param map Map<chatText, hoverText>
     * @return path to java runtime
     */
    fun sendMap(map: Map<String,String>){
        val component: IChatComponent = ChatComponentText("$MESSAGE_PREFIX")
        for (item in map){
            val clickableText: IChatComponent = ChatComponentText(item.key)
            val style =
                ChatStyle().setChatHoverEvent(
                    HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText(item.value))
                )
            clickableText.chatStyle = style

            component.appendSibling(clickableText)
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(component)
    }

    fun sendClickableList(list: List<String>) {
        val component: IChatComponent = ChatComponentText("$MESSAGE_PREFIX")
        for (item in list){
            val clickableText: IChatComponent = ChatComponentText("§6$item§8, ")
            val style =
                ChatStyle().setChatClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ac $item")).setChatHoverEvent(
                    HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText(item))
                )
            clickableText.chatStyle = style

            component.appendSibling(clickableText)
        }

        Minecraft.getMinecraft().thePlayer.addChatMessage(component)
    }

    fun sendClickableMessage(text: String, command: String?, hoverText: String?) {
        val clickableText: IChatComponent = ChatComponentText(MESSAGE_PREFIX + text)
        val style =
            ChatStyle().setChatClickEvent(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)).setChatHoverEvent(
                HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText(hoverText))
            )
        clickableText.chatStyle = style
        Minecraft.getMinecraft().thePlayer.addChatMessage(clickableText)
    }

    fun sendLink(text: String?, link: String?) {
        val clickableText: IChatComponent = ChatComponentText(text)
        val style = ChatStyle().setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, link))
        clickableText.chatStyle = style
        Minecraft.getMinecraft().thePlayer.addChatMessage(clickableText)
    }

    fun sendHoverMessage(text: String?, hoverText: String?) {
        val clickableText: IChatComponent = ChatComponentText(text)
        val style = ChatStyle().setChatHoverEvent(
            HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText(hoverText))
        )
        clickableText.chatStyle = style
        Minecraft.getMinecraft().thePlayer.addChatMessage(clickableText)
    }

    fun sendDataMessage(text: String) {
        var maxWidth = 1
        val sda = TextUtil.stripColor(text)?.split("\n".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        if (sda != null) {
            for (line in sda) {
                if (line.length > maxWidth) {
                    maxWidth = line.length
                }
            }
        }
        if (maxWidth > 50) {
            maxWidth = 50
        }
        val eti = StringUtils.repeat("-", maxWidth)
        val content = """
             ${EnumChatFormatting.STRIKETHROUGH}$eti
             $text
             ${EnumChatFormatting.STRIKETHROUGH}$eti
             """.trimIndent()
        sendMessage(content, false)
    }

}