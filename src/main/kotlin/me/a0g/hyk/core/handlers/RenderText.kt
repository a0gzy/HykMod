package me.a0g.hyk.core.handlers

import me.a0g.hyk.core.components.PositionDouble
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
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
class RenderText(text: String, pos: PositionDouble, color: Color): Gui() {

    val mc: Minecraft = Minecraft.getMinecraft()

    init {
        try {
            val scaleReset = pos.scale.pow(-1.0)
            GL11.glScaled(pos.scale, pos.scale, pos.scale)
            pos.y -= (mc.fontRendererObj.FONT_HEIGHT * pos.scale)
            for (line in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                pos.y += (mc.fontRendererObj.FONT_HEIGHT * pos.scale).toInt()
                mc.fontRendererObj.drawString(
                    line,
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