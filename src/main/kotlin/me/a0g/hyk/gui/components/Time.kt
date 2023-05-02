package me.a0g.hyk.gui.components

import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.animate
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.OutlineEffect
import me.a0g.hyk.core.components.PositionDouble
import java.awt.Color

class Time(text: String) : PositionComponent(text) {

    fun applyMethods(){
        this.applySame()
        this.onMouseDrag { _, _, _ ->
            val pos = PositionDouble(config.timePos)
            pos.x = getLeft().toDouble(); pos.y = getTop().toDouble();
            config.timePos = pos.toString()
        }.onMouseScroll {
            val pos = PositionDouble(config.timePos)
            pos.scale = getTextScale().toDouble()
            config.timePos = pos.toString()
        }
    }
}