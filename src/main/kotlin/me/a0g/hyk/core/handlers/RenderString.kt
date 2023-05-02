package me.a0g.hyk.core.handlers

import me.a0g.hyk.HykX
import me.a0g.hyk.core.components.PositionDouble
import me.a0g.hyk.utils.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.pow

/**
 * Renders Text in gui
 * @author a0g
 *
 * @param text text to render
 * @param pos where to render PositionDouble(x,y,scale)
 * @param color color of text
 * @constructor
 */
class RenderString(var text: String, var pos: PositionDouble, var color: Color): Gui() {

    val mc: Minecraft = Minecraft.getMinecraft()

    init {
        try {
            val scaleReset = pos.scale.pow(-1.0)
            GL11.glScaled(pos.scale, pos.scale, pos.scale)
            pos.y -= (mc.fontRendererObj.FONT_HEIGHT * pos.scale)
            for (line in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                pos.y += (mc.fontRendererObj.FONT_HEIGHT * pos.scale).toInt()
                var lineText = line

//                val ite = Item.getByNameOrId(Items.bow.registryName)
//                val item = Item.getByNameOrId("wheat")
//                val itemStack = ItemStack(item)
//                val item = ItemStack(Items.bow)
                if(line.contains("&&[{].+[}]&&".toRegex())){
                    val itemPattern = Regex("(?<text>[^&]+)?&&[{](?<item>[\\w]+)[}]&&")
                    val matches = itemPattern.findAll(line)
                    var lineX = 0

                    matches.forEach {
                        val matchText = it.groupValues[1]
                        val matchItem = Item.getByNameOrId( it.groupValues[2] )
                        val splitLineWidth = mc.fontRendererObj.getStringWidth(matchText) * pos.scale
                        lineX += splitLineWidth.toInt()
                        val itemWidth = (mc.fontRendererObj.getStringWidth("   ") * pos.scale).toInt()

                        RenderUtil.renderItem(ItemStack(matchItem), ( (pos.x + lineX ) / pos.scale), ( (pos.y - 1 * pos.scale) / pos.scale) , 0.7 )
                        lineX += itemWidth
                    }

                    lineText = lineText.replace("&&[{](\\w)+[}]&&".toRegex(),"   ")
                }



                mc.fontRendererObj.drawString(
                    lineText,
                    (pos.x / pos.scale).toFloat(),
                    (pos.y / pos.scale).toFloat(),
                    color.rgb,
                    true
                )
            }
            GL11.glScaled(scaleReset, scaleReset, pos.scale)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Renders white text in gui
     * @author a0g
     *
     * @param text text to render
     * @param pos PositionDouble(x,y,scale)
     * @constructor
     */
    constructor(text: String, pos: PositionDouble) : this(text, pos, Color.WHITE)


}