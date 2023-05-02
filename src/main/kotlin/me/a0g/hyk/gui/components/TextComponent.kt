package me.a0g.hyk.gui.components

import gg.essential.elementa.UIComponent
import gg.essential.elementa.UIConstraints
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.width
import gg.essential.elementa.state.BasicState
import gg.essential.universal.UMatrixStack
import me.a0g.hyk.core.components.PositionDouble
import me.a0g.hyk.core.handlers.RenderString
import me.a0g.hyk.utils.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class TextComponent(var text: String) : UIComponent() {

    private val verticallyCenteredState = constraints.y is CenterConstraint
    private val fontProviderState = constraints.fontProvider

    private val verticallyCenteredSta = constraints.asState { y is CenterConstraint }


    var textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text.replace("&&[{](\\w)+[}]&&".toRegex(),"   ")) * this.getTextScale()
    var textHeight = ( Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2 ) * this.getTextScale()

    init {
        val above = (if (verticallyCenteredState) fontProviderState.getBelowLineHeight() else 0f)
        val center = fontProviderState.getBaseLineHeight()
        val below = fontProviderState.getBelowLineHeight()

        text.width()

        setWidth(textWidth.pixels())
//        setHeight(textHeight.pixels())
        setHeight((above + center + below).pixels())



    }

    private fun <T> UIConstraints.asState(selector: UIConstraints.() -> T) = BasicState(selector(constraints)).also {
        constraints.addObserver { _, _ -> it.set(selector(constraints)) }
    }

    override fun draw(matrixStack: UMatrixStack) {
        beforeDraw(matrixStack)

        val scale = this.getTextScale()
        val x = this.getLeft()
        val y = this.getTop()  + (if (verticallyCenteredState) fontProviderState.getBelowLineHeight() * scale else 0f)

        val pos = PositionDouble(x, y, scale )

        RenderString(text, pos)

        super.draw(matrixStack)

    }

}