package me.a0g.hyk.core.features.hypixel

import gg.essential.api.utils.Multithreading
import kotlinx.coroutines.internal.artificialFrame
import me.a0g.hyk.HykX
import me.a0g.hyk.utils.MessageUtil
import me.a0g.hyk.utils.MinecraftUtil
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.FMLLog
import java.util.*

class BuildBattleHelper {

    var theme: String = ""
    var words: List<String> = mutableListOf<String>()
    var correct = false

    fun updateTheme(theme: String) {
        this.theme = theme.lowercase(Locale.getDefault())
        this.words = getCurrentWords()
        sendWordsInChat()
    }

    private fun sendWordsInChat() {
        if (words.size == 1 && HykX.config.bbhAutoSend) {
            Multithreading.runAsync {
                correct = true
                MessageUtil.sendMessage("Theme is: " + words[0])
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/ac " + words[0])
            }
        } else if (words.isNotEmpty()) {
            Multithreading.runAsync {
                MessageUtil.sendClickableList(words)
            }
        }
    }


    private fun getCurrentWords(): List<String> {
        val words: MutableList<String> = ArrayList()
        val themeLength = theme.length
        for (bbhWord in HykX.bbhWords) {
            val bbhWordLength = bbhWord.length
            if (bbhWord.contains(" ") != theme.contains(" ")) continue
            if (themeLength == bbhWordLength) {
                val themeNoUnder = theme.replace("[_]".toRegex(), "")
                val themeNoUnderLength = themeNoUnder.length
                var themeIntCounter = 0

                for (i in 0 until themeLength) {
                    val themeChar = theme[i]
                    val bbhWordChar = bbhWord[i]
                    if (themeChar != '_' && themeChar == bbhWordChar) {
                        themeIntCounter++
                        if (themeIntCounter == themeNoUnderLength) {
                            if (!words.contains(bbhWord)) {
                                // FMLLog.info("added" + bbhWord);
                                words.add(bbhWord)
                            }
                        }
                    }
                }
            }
        }
        return words
    }

}