package me.a0g.hyk.gui.components

import me.a0g.hyk.core.components.PositionDouble

class Fps(text: String) : PositionComponent(text) {

    fun applyMethods(){
        this.applySame()
        this.onMouseDrag { _, _, _ ->
            val pos = PositionDouble(config.fpsPos)
            pos.x = getLeft().toDouble(); pos.y = getTop().toDouble();
            config.fpsPos = pos.toString()
        }.onMouseScroll {
            val pos = PositionDouble(config.fpsPos)
            pos.scale = getTextScale().toDouble()
            config.fpsPos = pos.toString()
        }
    }

}