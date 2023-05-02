package me.a0g.hyk.core.handlers

import com.google.common.collect.Iterables
import com.google.common.collect.Lists
import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.Score
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.StringUtils
import java.util.stream.Collectors

object ScoreboardHandler {

    fun cleanSB(scoreboard: String?): String {
        val nvString = StringUtils.stripControlCodes(scoreboard).toCharArray()
        val cleaned = StringBuilder()
        for (c in nvString) {
            if (c.code in 21..126) {
                cleaned.append(c)
            }
        }
        return cleaned.toString()
    }

    fun getSidebarLines(): List<String> {
        val lines: MutableList<String> = ArrayList()
        val scoreboard = Minecraft.getMinecraft().theWorld.scoreboard ?: return lines
        val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return lines
        var scores = scoreboard.getSortedScores(objective)
        val list = scores.stream()
            .filter { input: Score? ->
                input != null && input.playerName != null && !input.playerName
                    .startsWith("#")
            }
            .collect(Collectors.toList())
        scores = if (list.size > 15) {
            Lists.newArrayList(Iterables.skip(list, scores.size - 15))
        } else {
            list
        }
        for (score in scores) {
            val team = scoreboard.getPlayersTeam(score.playerName)
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.playerName))
        }
        return lines
    }

}