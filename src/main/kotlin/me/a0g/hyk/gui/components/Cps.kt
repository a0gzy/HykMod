package me.a0g.hyk.gui.components

import me.a0g.hyk.core.components.PositionDouble

class Cps(text: String) : PositionComponent(text) {

    fun applyMethods(){
        this.applySame()
        this.onMouseDrag { _, _, _ ->
            val pos = PositionDouble(config.cpsPos)
            pos.x = getLeft().toDouble(); pos.y = getTop().toDouble();
            config.cpsPos = pos.toString()
        }.onMouseScroll {
            val pos = PositionDouble(config.cpsPos)
            pos.scale = getTextScale().toDouble()
            config.cpsPos = pos.toString()
        }
    }

}