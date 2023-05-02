package me.a0g.hyk.gui.components

import gg.essential.elementa.UIComponent
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UMatrixStack
import me.a0g.hyk.HykX
import me.a0g.hyk.core.components.PositionDouble
import me.a0g.hyk.utils.RenderUtil
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.awt.Color


class ArmorComponent : UIComponent() {

    init {

    }

    var config = HykX.config
    var drag: Boolean = false
    var hover: Boolean = false
    var dragOffset: Pair<Float, Float> = 0f to 0f

    fun applyMy() {
        this.enableEffect(OutlineEffect(Color.BLACK, 2f))
        this.onMouseEnter {
            hover = true
            enableEffect(OutlineEffect(Color.LIGHT_GRAY, 2f))
        }.onMouseLeave {
            hover = false
            enableEffect(OutlineEffect(Color.BLACK, 2f))
        }.onMouseScroll { event ->
            if (hover) {
                // val newScale: Double = config.armorPositionScale + (event.delta / 2)
                val pos = PositionDouble(config.armorPos)
                val newScale: Double = pos.scale + (event.delta / 10)
                if (newScale > 0) {
                    // setTextScale(newScale.pixels())
//                    config.armorPositionScale = newScale.toFloat()
                    pos.scale = newScale
                    config.armorPos = pos.toString()
                    val width = 16 * newScale
                    val height = 64 * newScale
                    setWidth( width.pixels())
                    setHeight( height.pixels())
                }
            }
        }.onMouseClick { event ->
            drag = true;

            dragOffset = event.absoluteX to event.absoluteY
        }.onMouseRelease {
            drag = false;
        }.onMouseDrag { mouseX, mouseY, mouseButton ->

            if (!drag) return@onMouseDrag

            val absoluteX = mouseX + getLeft()
            val absoluteY = mouseY + getTop()

            val deltaX = absoluteX - dragOffset.first
            val deltaY = absoluteY - dragOffset.second

            dragOffset = absoluteX to absoluteY
            val parentWidth = parent.getWidth()
            val parentHeight = parent.getHeight()

            var newX = getLeft() + deltaX
            var newY = getTop() + deltaY
            if (newX < 0) newX = 0F
            if (newX + getWidth() > parentWidth) newX = parentWidth - getWidth()
            if (newY < 0) newY = 0F
            if (newY + getHeight() > parentHeight) newY = parentHeight - getHeight()

            setX(newX.pixels())
            setY(newY.pixels())

            //config.armorPositionX = newX.toInt()
            //config.armorPositionY = newY.toInt()

            val pos = PositionDouble(config.armorPos)
            pos.x = newX.toDouble()
            pos.y = newY.toDouble()
            config.armorPos = pos.toString()
        }
    }

    override fun draw(matrixStack: UMatrixStack) {
        beforeDraw(matrixStack)
        super.draw(matrixStack)
        val x = this.getLeft().toInt()
        val y = this.getTop().toInt()
        val pos = PositionDouble(config.armorPos)
        val scale = pos.scale

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);

        for (i in 0..3) {
            val armor = ItemStack(Item.getItemById(313 - i));
            val offset = (-16 * i) + 48;
            RenderUtil.renderArmor(armor, x / scale, y / scale + offset);
        }
        GlStateManager.popMatrix();

    }

}