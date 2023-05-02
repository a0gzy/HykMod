package me.a0g.hyk.core.features.skyblock

import me.a0g.hyk.utils.HypixelUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11

class CompletedCommissions {

    @SubscribeEvent
    fun onGuiRender(event: BackgroundDrawnEvent) {
        if (event.gui is GuiChest) {
            val inventory = event.gui as GuiChest
            val containerChest = inventory.inventorySlots
            if (containerChest is ContainerChest) {
                val mc = Minecraft.getMinecraft()
                val invSlots = inventory.inventorySlots.inventorySlots
                val displayName = containerChest.lowerChestInventory.displayName.unformattedText.trim { it <= ' ' }
                val chestSize = inventory.inventorySlots.inventorySlots.size

                //Completed commissions
                if (displayName.startsWith("Commissions") && HypixelUtil.checkForSkyBlock()) {
                    for (slot in invSlots) {
                        val item = slot.stack ?: return
                        if (!item.hasTagCompound()) return
                        if (item.tagCompound.hasNoTags()) return
                        if (item.displayName.contains("#") && item.tagCompound.getCompoundTag("display").getTag("Lore")
                                .toString().contains("COMPLETED")
                        ) {
                            var colour = -0x40a3e0cb // weird magenta
                            colour = -0x4029dbc0 // Red
                            //colour = 0xFF0000FF; // Red my
                            drawOnSlot(chestSize, slot.xDisplayPosition, slot.yDisplayPosition, colour)
                        }
                    }
                }
            }
        }
    }

    private fun drawOnSlot(size: Int, xSlotPos: Int, ySlotPos: Int, colour: Int) {
        val sr = ScaledResolution(Minecraft.getMinecraft())
        val guiLeft = (sr.scaledWidth - 176) / 2
        val guiTop = (sr.scaledHeight - 222) / 2
        val x = guiLeft + xSlotPos
        var y = guiTop + ySlotPos
        // Move down when chest isn't 6 rows
        if (size != 90) y += (6 - (size - 36) / 9) * 9
        GL11.glTranslated(0.0, 0.0, 1.0)
        Gui.drawRect(x, y, x + 16, y + 16, colour)
        GL11.glTranslated(0.0, 0.0, -1.0)
    }

}