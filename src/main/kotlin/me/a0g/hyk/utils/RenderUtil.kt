package me.a0g.hyk.utils

import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import me.a0g.hyk.HykX
import me.a0g.hyk.core.components.PositionBlockDouble
import me.a0g.hyk.core.components.PositionDouble
import me.a0g.hyk.core.handlers.RenderText
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

object RenderUtil {
    private var fontRenderer: FontRenderer = Minecraft.getMinecraft().fontRendererObj

    private fun drawFilledBoundingBox(aabb: AxisAlignedBB, c: Color, alphaMultiplier: Float) {
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.disableTexture2D()
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        GlStateManager.color(c.red / 255.0f, c.green / 255.0f, c.blue / 255.0f, c.alpha / 255.0f * alphaMultiplier)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        tessellator.draw()
        GlStateManager.color(
            c.red / 255.0f * 0.8f,
            c.green / 255.0f * 0.8f,
            c.blue / 255.0f * 0.8f,
            c.alpha / 255.0f * alphaMultiplier
        )
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()
        GlStateManager.color(
            c.red / 255.0f * 0.9f,
            c.green / 255.0f * 0.9f,
            c.blue / 255.0f * 0.9f,
            c.alpha / 255.0f * alphaMultiplier
        )
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    /**
     * Highlights block in the world
     *
     * @param pos position of the block
     * @param color color for highlight
     * @param partialTicks minecraft partialTicks
     */
    fun highlightBlock(pos: BlockPos, color: Color, partialTicks: Float) {
        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks
        val x = pos.x - viewerX
        val y = pos.y - viewerY
        val z = pos.z - viewerZ
        GlStateManager.disableDepth()
        GlStateManager.disableCull()
        GlStateManager.disableLighting()
        drawFilledBoundingBox(AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), color, 1.0f)
        GlStateManager.enableLighting()
        GlStateManager.enableDepth()
        GlStateManager.enableCull()
    }

    fun highlightBlockLines(pos: BlockPos, color: Color, partialTicks: Float) {
        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks
        val x = pos.x - viewerX
        val y = pos.y - viewerY
        val z = pos.z - viewerZ
        GlStateManager.disableDepth()
        GlStateManager.disableCull()
        GlStateManager.disableLighting()
        RenderGlobal.drawSelectionBoundingBox(AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0),)
        GlStateManager.enableLighting()
        GlStateManager.enableDepth()
        GlStateManager.enableCull()
    }

    fun drawTextWithPanel(text: String, x: Double, y: Double, scale: Double){

        RenderText(text, PositionDouble(x,y,scale))

        val textHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * text.split("\n").size
        var maxTextWidth = 0
        text.split("\n").forEach {
            val width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(it)
            if(width > maxTextWidth)
                maxTextWidth = width
        }

        drawBackPanel(x - 5, x + maxTextWidth * scale + 5, y - 5, y + textHeight * scale + 5, -99.0, Color.BLACK.withAlpha(155))

    }

    fun drawTextWithPanel(text: String, x: Double, y: Double, scale: Double, color: Color){

        RenderText(text, PositionDouble(x,y,scale))

        val textHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * text.split("\n").size
        var maxTextWidth = 0
        text.split("\n").forEach {
            val width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(it)
            if(width > maxTextWidth)
                maxTextWidth = width
        }

        drawBackPanel(x - 5, x + maxTextWidth * scale + 5, y - 5, y + textHeight * scale + 5, -99.0, color)

    }

    fun drawBackPanel(left: Double, right: Double, top: Double, bot: Double, zLevel: Double, color: Color){
        GlStateManager.pushMatrix()

        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.shadeModel(7425);

        GlStateManager.color(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, color.alpha / 255.0f)

        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        worldRenderer.begin(7, DefaultVertexFormats.POSITION)
        worldRenderer.pos(left, bot, zLevel).endVertex()
        worldRenderer.pos(right, bot, zLevel).endVertex()
        worldRenderer.pos(right, top, zLevel).endVertex()
        worldRenderer.pos(left, top, zLevel).endVertex()
        tessellator.draw()

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }

    fun drawRomb(blockPos: BlockPos, color: Color, depth: Boolean, partialTicks: Float){
        val render = Minecraft.getMinecraft().renderViewEntity
        val worldRenderer = Tessellator.getInstance().worldRenderer
        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks
        val x = blockPos.x - viewerX
        val y = blockPos.y - viewerY
        val z = blockPos.z - viewerZ

        GlStateManager.pushMatrix()
        GlStateManager.translate(-viewerX, -viewerY, -viewerZ)
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        if (!depth) {
            GL11.glDisable(2929)
            GlStateManager.depthMask(false)
        }
        GlStateManager.color(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, color.alpha / 255.0f)
        worldRenderer.begin(3, DefaultVertexFormats.POSITION)

        val rombY = blockPos.y + 1.02

        worldRenderer.pos(blockPos.x + 0.5, rombY, blockPos.z + 0.0).endVertex()
        worldRenderer.pos(blockPos.x + 1.0, rombY, blockPos.z + 0.5).endVertex()
        worldRenderer.pos(blockPos.x + 0.5, rombY, blockPos.z + 1.0).endVertex()
        worldRenderer.pos(blockPos.x + 0.0, rombY, blockPos.z + 0.5).endVertex()
        worldRenderer.pos(blockPos.x + 0.5, rombY, blockPos.z + 0.0).endVertex()

        Tessellator.getInstance().draw()
        GlStateManager.translate(viewerX, viewerY, viewerZ)
        if (!depth) {
            GL11.glEnable(2929)
            GlStateManager.depthMask(true)
        }
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }

    fun draw3DLine(pos1: Vec3, pos2: Vec3, color: Color, lineWidth: Int, depth: Boolean, partialTicks: Float) {
        val render = Minecraft.getMinecraft().renderViewEntity
        val worldRenderer = Tessellator.getInstance().worldRenderer
        val realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks
        val realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks
        val realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks
        GlStateManager.pushMatrix()
        GlStateManager.translate(-realX, -realY, -realZ)
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GL11.glLineWidth(lineWidth.toFloat())
        if (!depth) {
            GL11.glDisable(2929)
            GlStateManager.depthMask(false)
        }
        GlStateManager.color(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, color.alpha / 255.0f)
        worldRenderer.begin(3, DefaultVertexFormats.POSITION)
        worldRenderer.pos(pos1.xCoord, pos1.yCoord, pos1.zCoord).endVertex()
        worldRenderer.pos(pos2.xCoord, pos2.yCoord, pos2.zCoord).endVertex()
        Tessellator.getInstance().draw()
        GlStateManager.translate(realX, realY, realZ)
        if (!depth) {
            GL11.glEnable(2929)
            GlStateManager.depthMask(true)
        }
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }

    fun renderWaypointText(str: String?, X: Double, Y: Double, Z: Double, partialTicks: Float, distance: Boolean) {
        GlStateManager.alphaFunc(516, 0.1f)
        GlStateManager.pushMatrix()
        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks
        var x = X - viewerX
        var y = Y - viewerY - viewer.eyeHeight
        var z = Z - viewerZ
        val distSq = x * x + y * y + z * z
        val dist = sqrt(distSq)
        if (distSq > 144.0) {
            x *= 12.0 / dist
            y *= 12.0 / dist
            z *= 12.0 / dist
        }
        GlStateManager.translate(x, y, z)
        GlStateManager.translate(0.0f, viewer.eyeHeight, 0.0f)
        drawNametag(str,false)
        GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        if(distance) {
            GlStateManager.translate(0.0f, -0.25f, 0.0f)
            GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
            GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
            drawNametag(EnumChatFormatting.YELLOW.toString() + dist.roundToInt() + " blocks",false)
        }
        GlStateManager.popMatrix()
        GlStateManager.disableLighting()
    }

    fun renderWaypointText(str: String?, X: Double, Y: Double, Z: Double, depth: Boolean, partialTicks: Float) {
        GlStateManager.alphaFunc(516, 0.1f)
        GlStateManager.pushMatrix()
        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks
        var x = X - viewerX
        var y = Y - viewerY
        var z = Z - viewerZ
        GlStateManager.translate(x, y, z)
        drawNametag(str,depth)
        GlStateManager.popMatrix()
        GlStateManager.disableLighting()
    }

    fun renderWaypointText(str: String?, X: Double, Y: Double, Z: Double, partialTicks: Float, distance: Boolean,depth: Boolean) {
        GlStateManager.alphaFunc(516, 0.1f)
        GlStateManager.pushMatrix()
        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks
        var x = X - viewerX
        var y = Y - viewerY - viewer.eyeHeight
        var z = Z - viewerZ
        val distSq = x * x + y * y + z * z
        val dist = sqrt(distSq)
        if (distSq > 144.0) {
            x *= 12.0 / dist
            y *= 12.0 / dist
            z *= 12.0 / dist
        }
        GlStateManager.translate(x, y, z)
        GlStateManager.translate(0.0f, viewer.eyeHeight, 0.0f)
        drawNametag(str,depth)
        GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        if(distance) {
            GlStateManager.translate(0.0f, -0.25f, 0.0f)
            GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
            GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
            drawNametag(EnumChatFormatting.YELLOW.toString() + dist.roundToInt() + " blocks",depth)
        }
        GlStateManager.popMatrix()
        GlStateManager.disableLighting()
    }

    fun drawNametag(str: String?,depth:Boolean) {
        val f = 1.6f
        val f1 = 0.016666668f * f
        GlStateManager.pushMatrix()
        GL11.glNormal3f(0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(Minecraft.getMinecraft().renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        GlStateManager.scale(-f1, -f1, f1)
        GlStateManager.disableLighting()
//        GlStateManager.depthMask(depth)
//        GlStateManager.depthMask(false)
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        if (!depth) {
            GL11.glDisable(2929)
            GlStateManager.depthMask(false)
        }else {
            GlStateManager.depthMask(false)
        }

        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        val i = 0
        val j = fontRenderer.getStringWidth(str) / 2
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos((-j - 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        worldrenderer.pos((-j - 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        worldrenderer.pos((j + 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        worldrenderer.pos((j + 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
//        fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, i, 553648127)
        fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, i, -1)
//        GlStateManager.depthMask(true)
//        GlStateManager.depthMask(!depth)
        if (!depth) {
            GL11.glEnable(2929)
            GlStateManager.depthMask(true)
        }else {
            GlStateManager.depthMask(true)
        }
        GlStateManager.enableLighting()
        GlStateManager.disableBlend()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }

    /**
     * Renders armor in gui
     *
     * @param item item to render
     * @param x x position of the gui
     * @param y y position of the gui
     */
    fun renderArmor(item: ItemStack?, x: Double, y: Double) {
        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        RenderHelper.enableGUIStandardItemLighting()
        GlStateManager.translate(x, y, -100.0)
        var font: FontRenderer? = null
        if (item != null) {
            font = item.item.getFontRenderer(item)
        }
        if (font == null) {
            font = Minecraft.getMinecraft().fontRendererObj
        }
        Minecraft.getMinecraft().renderItem.renderItemIntoGUI(item, 0, 0)
        Minecraft.getMinecraft().renderItem.renderItemOverlayIntoGUI(font, item, 0, 0, "")
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.popMatrix()
    }


    fun renderItem(item: ItemStack, x: Double, y: Double, scale: Double) {
        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        RenderHelper.enableGUIStandardItemLighting()
        GlStateManager.translate(x, y, -100.0)

        val scaleReset = scale.pow(-1.0)
        GlStateManager.scale(scale, scale, scale)

        Minecraft.getMinecraft().renderItem.renderItemIntoGUI(item, 0, 0)
        GlStateManager.scale(scaleReset, scaleReset, scale)

        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.popMatrix()
    }

    fun renderItem(itemStack: ItemStack?, x: Int, y: Int) {
        RenderHelper.enableGUIStandardItemLighting()
        GlStateManager.enableDepth()
        Minecraft.getMinecraft().renderItem.renderItemAndEffectIntoGUI(itemStack, x, y)
    }

    fun renderItem(itemStack: ItemStack?, x: Int, y: Int, toScale: Boolean) {

        GlStateManager.pushMatrix()

        RenderHelper.enableGUIStandardItemLighting()
        GlStateManager.enableDepth()

        GlStateManager.translate(x.toDouble(), y.toDouble(), -100.0)

        val scale = 0.6
        val scaleReset = scale.pow(-1.0)
        if (toScale) {
            GlStateManager.scale(scale, scale, scale)
        }

        Minecraft.getMinecraft().renderItem.renderItemAndEffectIntoGUI(itemStack, 0, 0)

        if (toScale) {
            GlStateManager.scale(scaleReset, scaleReset, scale)
        }

        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.popMatrix()
    }



    fun renderStringItem(text: String,pos: PositionDouble){
        try {
            val mc = Minecraft.getMinecraft()
            val scaleReset = pos.scale.pow(-1.0)
            GL11.glScaled(pos.scale, pos.scale, pos.scale)
            pos.y -= (mc.fontRendererObj.FONT_HEIGHT * pos.scale)
            for (line in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                pos.y += (mc.fontRendererObj.FONT_HEIGHT * pos.scale).toInt()
                var lineText = line

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

                        renderItem(ItemStack(matchItem), ( (pos.x + lineX ) / pos.scale), ( (pos.y - 2 * pos.scale) / pos.scale) , 1.0 )
//                        HykX.logger.info("$lineX")
                        lineX += itemWidth
                    }

                    lineText = lineText.replace("&&[{](\\w)+[}]&&".toRegex(),"   ")
                }

                mc.fontRendererObj.drawString(
                    lineText,
                    (pos.x / pos.scale).toFloat(),
                    (pos.y / pos.scale).toFloat(),
                    Color.WHITE.rgb,
                    true
                )
            }
            GL11.glScaled(scaleReset, scaleReset, pos.scale)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun renderItem(item: ItemStack, x: Double, y: Double, scale: Double , matrix: UMatrixStack) {
        matrix.push()
        UGraphics.enableLighting()
        matrix.translate(x, y, -100.0)

        val scaleReset = scale.pow(-1.0)
        matrix.scale(scale, scale, scale)

        Minecraft.getMinecraft().renderItem.renderItemIntoGUI(item, 0, 0)
        matrix.scale(scaleReset, scaleReset, scale)

        matrix.pop()
    }

    fun renderStringItem(text: String,pos: PositionDouble, matrix: UMatrixStack){
        try {
            val mc = Minecraft.getMinecraft()
            val scaleReset = pos.scale.pow(-1.0)
            matrix.scale(pos.scale, pos.scale, pos.scale)
            pos.y -= (mc.fontRendererObj.FONT_HEIGHT * pos.scale)
            for (line in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                pos.y += (mc.fontRendererObj.FONT_HEIGHT * pos.scale).toInt()
                var lineText = line

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

                        renderItem(ItemStack(matchItem), ( (pos.x + lineX ) / pos.scale), ( (pos.y - 1 * pos.scale) / pos.scale) , 0.6)
//                        HykX.logger.info("$lineX")
                        lineX += itemWidth
                    }

                    lineText = lineText.replace("&&[{](\\w)+[}]&&".toRegex(),"   ")
                }

                mc.fontRendererObj.drawString(
                    lineText,
                    (pos.x / pos.scale).toFloat(),
                    (pos.y / pos.scale).toFloat(),
                    Color.WHITE.rgb,
                    true
                )
            }
            matrix.scale(scaleReset, scaleReset, pos.scale)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}