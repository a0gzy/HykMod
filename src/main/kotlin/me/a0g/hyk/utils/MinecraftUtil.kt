package me.a0g.hyk.utils

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import me.a0g.hyk.HykX
import me.a0g.hyk.mixins.accessors.GuiPlayerTabOverlayAccessor
import net.minecraft.block.Block
import net.minecraft.block.BlockDirt
import net.minecraft.block.BlockGrass
import net.minecraft.block.BlockStone
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItemFrame
import net.minecraft.init.Blocks
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.network.Packet
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.util.*
import net.minecraft.world.IBlockAccess
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

object MinecraftUtil {

    fun getItemLore(itemStack: ItemStack): List<String> {
        if (itemStack.hasTagCompound() && itemStack.tagCompound.hasKey("display", 10)) { // 10 -> Compound
            val display = itemStack.tagCompound.getCompoundTag("display")
            if (display.hasKey("Lore", 9)) { // 9 -> List
                val lore = display.getTagList("Lore", 8) // 8 -> String
                val loreAsList: MutableList<String> = ArrayList()
                for (lineNumber in 0 until lore.tagCount()) {
                    loreAsList.add(lore.getStringTagAt(lineNumber))
                }
                return loreAsList
            }
        }
        return emptyList()
    }

    fun getTabList(): List<String> {
        if (Minecraft.getMinecraft().thePlayer == null && Minecraft.getMinecraft().theWorld == null) return emptyList()
        val mc = Minecraft.getMinecraft()
        assert(mc.thePlayer != null)
        val netHandler = mc.thePlayer.sendQueue
        val tabList = Minecraft.getMinecraft().ingameGUI.tabList
        val fullList = (tabList as GuiPlayerTabOverlayAccessor).field_175252_a.sortedCopy(netHandler.playerInfoMap)
        val tab = ArrayList<String>()
        for (networkPlayerInfo in fullList) {
            tab.add(tabList.getPlayerName(networkPlayerInfo))
        }
        return tab
    }

    /**
     * Get gui name
     *
     * @param gui gui where to check name
     * @return gui name : String
     */
    fun getGuiName(gui: GuiScreen?): String {
        return if (gui is GuiChest) (gui.inventorySlots as ContainerChest).lowerChestInventory.displayName.unformattedText else ""
    }

    /**
     * Gets inventory name
     *
     * @return inventory name
     */
    fun getInventoryName(): String {
        return if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) ""
        else (Minecraft.getMinecraft().thePlayer.openContainer.inventorySlots[0] as Slot).inventory.name
    }

    fun replaceUserName(text: String): String {
        if(HykX.config.customName.isNotEmpty() && Minecraft.getMinecraft()?.thePlayer != null) {
            var changedText = text
            val namePattern = Regex("(?<color>(§.)+)?${Minecraft.getMinecraft().thePlayer.name}")
            val matches = namePattern.findAll(text)
            val nameFromConfig = HykX.config.customName.replace("&","§")

            matches.forEach {
                val colors = it.groupValues[1]
                val nameReplaced = if(colors.isEmpty()) "$nameFromConfig§r" else nameFromConfig + colors
                changedText = text.replace(it.groupValues[0],nameReplaced)
            }

            return changedText
        }

        return text
    }

}