package me.a0g.hyk.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import me.a0g.hyk.HykX
import me.a0g.hyk.core.components.PositionDouble
import me.a0g.hyk.gui.components.*
import java.awt.Color

class Gui : WindowScreen(ElementaVersion.V2) {
    private val hyConfig = HykX.config

    override fun onScreenClose() {
        hyConfig.markDirty()
        hyConfig.writeData()

        super.onScreenClose()
    }

    private var sprint = Sprint(hyConfig.sprintText)
    private var time = Time("13:87")
    private var cps = Cps("CPS: 10")
    private var fps = Fps("FPS: 1024")
    private var armor = ArmorComponent()

    private var fryct = Fryct(hyConfig.fryctText + " 2d15h44m")

    init {

        if(hyConfig.fryctDisplay) {
            fryct.constrain {
                val pos = PositionDouble(hyConfig.fryctPos)
                x = pos.x.pixels()
                y = pos.y.pixels()
                textScale = pos.scale.pixels()
                color = Color.WHITE.toConstraint()
            }.childOf(window).applyMethods()
        }


        sprint.constrain {
            val pos = PositionDouble(hyConfig.sprintPos)
            x = pos.x.pixels()
            y = pos.y.pixels()
            textScale = pos.scale.pixels()
            color = Color.WHITE.toConstraint()
        }.childOf(window).applyMethods()

        time.constrain {
            val pos = PositionDouble(hyConfig.timePos)
            x = pos.x.pixels()
            y = pos.y.pixels()
            textScale = pos.scale.pixels()
            color = Color.WHITE.toConstraint()
        }.childOf(window).applyMethods()

        cps.constrain {
            val pos = PositionDouble(hyConfig.cpsPos)
            x = pos.x.pixels()
            y = pos.y.pixels()
            textScale = pos.scale.pixels()
            color = Color.WHITE.toConstraint()
        }.childOf(window).applyMethods()

        fps.constrain {
            val pos = PositionDouble(hyConfig.fpsPos)
            x = pos.x.pixels()
            y = pos.y.pixels()
            textScale = pos.scale.pixels()
            color = Color.WHITE.toConstraint()
        }.childOf(window).applyMethods()


        armor.constrain {
            val pos = PositionDouble(hyConfig.armorPos)
            width = ( 16 * pos.scale ).pixels()
            height = ( 64 * pos.scale ).pixels()
            x = pos.x.pixels()
            y = pos.y.pixels()
        }.childOf(window).applyMy()

        val reset = UIText("Reset").constrain {
            x = CenterConstraint()
            y = CenterConstraint()
        } childOf window effect OutlineEffect(Color.BLACK,2f)
        reset.onMouseClick {
            resetConfig()
        }.onMouseEnter {
            animate {
                setColorAnimation(Animations.OUT_EXP, 6.5f, Color(255, 0, 0).toConstraint())
            }
            enableEffect(OutlineEffect(Color.LIGHT_GRAY, 2f))
        }.onMouseLeave {
            animate {
                setColorAnimation(Animations.OUT_EXP, 3.0f, Color.WHITE.toConstraint())
            }
            enableEffect(OutlineEffect(Color.BLACK, 2f))
        }

    }

    private fun resetConfig(){
        hyConfig.setStandard()

        restorePreviousScreen()
    }



}