package me.a0g.hyk.gui.components

import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.animate
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.OutlineEffect
import me.a0g.hyk.HykX
import java.awt.Color

open class PositionComponent(text: String) : UIText(text) {

    var config = HykX.config
    var drag: Boolean = false
    var hover: Boolean = false
    var dragOffset: Pair<Float, Float> = 0f to 0f

    fun applySame() {
        this.enableEffect(OutlineEffect(Color.BLACK, 2f))
        this.onMouseEnter {
            hover = true
            animate {
                setColorAnimation(Animations.OUT_EXP, 6.5f, Color(129, 0, 204).toConstraint())
            }
            enableEffect(OutlineEffect(Color.LIGHT_GRAY, 2f))
        }.onMouseLeave {
            hover = false
            animate {
                setColorAnimation(Animations.OUT_EXP, 3.0f, Color.WHITE.toConstraint())
            }
            enableEffect(OutlineEffect(Color.BLACK, 2f))
        }
            .onMouseScroll { event ->
                if (hover) {
                    val newScale: Double = getTextScale() + (event.delta / 2)
                    if (newScale > 0) {
                        setTextScale(newScale.pixels())
                     //   config.sprintPositionScale = getTextScale()
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
                if(newX < 0) newX = 0F
                if(newX + getWidth() > parentWidth) newX = parentWidth - getWidth()
                if(newY < 0) newY = 0F
                if(newY + getHeight() > parentHeight) newY = parentHeight - getHeight()

                setX(newX.pixels())
                setY(newY.pixels())

             //   config.sprintPositionX = newX.toInt()
            //    config.sprintPositionY = newY.toInt()
            }
    }
}