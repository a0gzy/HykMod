package me.a0g.hyk.utils

import com.google.common.base.Functions
import com.google.common.collect.Iterables
import com.google.common.collect.Lists
import net.minecraft.client.Minecraft
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import java.util.*
import java.util.stream.Collectors

object TabCompletionUtil {

    fun getListOfStringsMatchingLastWord(p_175762_0_: Array<String>, p_175762_1_: Collection<*>): List<String>? {
        val s = p_175762_0_[p_175762_0_.size - 1]
        val list: MutableList<String> = Lists.newArrayList()
        if (!p_175762_1_.isEmpty()) {
            for (s2 in Iterables.transform(p_175762_1_, Functions.toStringFunction())) {
                if (doesStringStartWith(s, s2)) {
                    list.add(s2)
                }
            }
            if (list.isEmpty()) {
                for (`object` in p_175762_1_) {
                    if (`object` is ResourceLocation && doesStringStartWith(s, `object`.resourcePath)) {
                        list.add(`object`.toString())
                    }
                }
            }
        }
        return list
    }

    fun getListOfStringsMatchingLastWord(p_175762_0_: Array<String>, p_175762_1_: List<*>?): List<String> {
        val s = p_175762_0_[p_175762_0_.size - 1]
        val list: MutableList<String> = Lists.newArrayList()
        if (p_175762_1_ != null) {
            if (p_175762_1_.isNotEmpty()) {
                for (s2 in Iterables.transform(p_175762_1_, Functions.toStringFunction())) {
                    if (doesStringStartWith(s, s2)) {
                        list.add(s2)
                    }
                }
                if (list.isEmpty()) {
                    for (`object` in p_175762_1_) {
                        if ((`object` is ResourceLocation) && doesStringStartWith(s, `object`.resourcePath)) {
                            list.add(`object`.toString())
                        }
                    }
                }
            }
        }
        return list
    }

  //  fun getListOfStringsMatchingLastWord(p_175762_0_: Array<String>, p_175762_1_: Array<String?>): List<String>? {
  //      return getListOfStringsMatchingLastWord(p_175762_0_, Arrays.asList(*p_175762_1_))
  //  }

    /*fun getListOfStringsMatchingLastWord(p_175762_0_: Array<String>, p_175762_1_: List<String>?): List<String>? {
        return getListOfStringsMatchingLastWord(p_175762_0_, p_175762_1_)
    }*/

    private fun doesStringStartWith(original: String, region: String): Boolean {
        return region.regionMatches(0, original, 0, original.length, ignoreCase = true)
    }

    fun getTabUsernames(): List<String>? {
        val player = Minecraft.getMinecraft().thePlayer
        val playerNames: List<String> = Lists.newArrayList()
        return if (player == null) {
            playerNames
        } else player.sendQueue.playerInfoMap.stream().map { netPlayerInfo: NetworkPlayerInfo ->
            netPlayerInfo.gameProfile.name
        }.collect(Collectors.toList())
    }

    fun getLoadedPlayers(): List<EntityPlayer?>? {
        return Minecraft.getMinecraft().theWorld.playerEntities
    }

}