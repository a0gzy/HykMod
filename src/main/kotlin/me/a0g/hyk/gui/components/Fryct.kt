package me.a0g.hyk.gui.components

import me.a0g.hyk.core.components.PositionDouble

class Fryct(text: String) : PositionComponent(text) {

    fun applyMethods(){
        this.applySame()
        this.onMouseDrag { _, _, _ ->
            val pos = PositionDouble(config.fryctPos)
            pos.x = getLeft().toDouble(); pos.y = getTop().toDouble();
            config.fryctPos = pos.toString()
        }.onMouseScroll {
            val pos = PositionDouble(config.fryctPos)
            pos.scale = getTextScale().toDouble()
            config.fryctPos = pos.toString()
        }
    }

}