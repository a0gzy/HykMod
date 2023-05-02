package me.a0g.hyk.core

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.util.StringUtils
import org.lwjgl.opengl.GL11
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Renders Text in gui
 * @author a0g
 *
 * @param text text to render
 * @param x x position
 * @param y y position
 * @param scale scale of text
 * @constructor
 */
class TextRenderer(text: String, x: Int, y: Int, scale: Double) : Gui() {

    val mc = Minecraft.getMinecraft()

    init {
        var y = y
        try {
            val scaleReset = Math.pow(scale, -1.0)
            GL11.glScaled(scale, scale, scale)
            y -= (mc.fontRendererObj.FONT_HEIGHT * scale).toInt()
            for (line in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                y += (mc.fontRendererObj.FONT_HEIGHT * scale).toInt()
                mc.fontRendererObj.drawString(
                    line,
                    Math.round(x / scale).toInt().toFloat(),
                    Math.round(y / scale).toInt().toFloat(),
                    0xFFFFFF,
                    true
                )
            }
            GL11.glScaled(scaleReset, scaleReset, scale)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    constructor(text: String, x: Int, y: Int, scale: Double, color: Int) : this(text, x, y, scale) {
        var y = y
        val scaleReset = Math.pow(scale, -1.0)
        GL11.glScaled(scale, scale, scale)
        y -= (mc.fontRendererObj.FONT_HEIGHT * scale).toInt()
        for (line in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            y += (mc.fontRendererObj.FONT_HEIGHT * scale).toInt()
            mc.fontRendererObj.drawString(
                line,
                Math.round(x / scale).toInt().toFloat(),
                Math.round(y / scale).toInt().toFloat(),
                color,
                true
            )
        }
        GL11.glScaled(scaleReset, scaleReset, scaleReset)
    }

    constructor(text: String, x: Int, y: Int, scale: Double, color: Int, outline: Boolean) : this(text, x, y, scale) {
        var y = y
        val scaleReset = scale.pow(-1.0)
        GL11.glScaled(scale, scale, scale)
        y -= (mc.fontRendererObj.FONT_HEIGHT * scale).toInt()
        for (line in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            y += (mc.fontRendererObj.FONT_HEIGHT * scale).toInt()
            if (outline) {
                val noColorLine = StringUtils.stripControlCodes(line)
                mc.fontRendererObj.drawString(
                    noColorLine,
                    ((x / scale).roundToInt().toInt() - 1).toFloat(),
                    (y / scale).roundToInt().toInt().toFloat(),
                    0x000000,
                    false
                )
                mc.fontRendererObj.drawString(
                    noColorLine,
                    ((x / scale).roundToInt().toInt() + 1).toFloat(),
                    (y / scale).roundToInt().toInt().toFloat(),
                    0x000000,
                    false
                )
                mc.fontRendererObj.drawString(
                    noColorLine,
                    (x / scale).roundToInt().toInt().toFloat(),
                    ((y / scale).roundToInt().toInt() - 1).toFloat(),
                    0x000000,
                    false
                )
                mc.fontRendererObj.drawString(
                    noColorLine, (x / scale).roundToInt().toFloat(), ((y / scale).roundToInt().toInt() + 1).toFloat(),
                    0x000000, false
                )
                mc.fontRendererObj.drawString(
                    line, (x / scale).roundToInt().toFloat(), (y / scale).roundToInt().toInt().toFloat(),
                    color, false
                )
            } else {
                mc.fontRendererObj.drawString(
                    line, (x / scale).roundToInt().toInt().toFloat(), (y / scale).roundToInt().toInt().toFloat(),
                    color, true
                )
            }
        }
        GL11.glScaled(scaleReset, scaleReset, scaleReset)
    }


}