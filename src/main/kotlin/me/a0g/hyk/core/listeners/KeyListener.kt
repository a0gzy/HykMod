package me.a0g.hyk.core.listeners

import gg.essential.api.utils.GuiUtil
import me.a0g.hyk.HykX
import me.a0g.hyk.gui.Gui
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import org.lwjgl.input.Keyboard
import java.util.*

class KeyListener {

    private val config = HykX.config

    @SubscribeEvent
    fun onEvent(event: KeyInputEvent?) {
        val keyBindings = HykX.keyBindings

        // P
        if (keyBindings[0]!!.isPressed) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                GuiUtil.open(Gui())
                return
            }
            try {
                GuiUtil.open(Objects.requireNonNull(config.gui()))
            } catch (e: Exception) {
                e.printStackTrace()
                config.preload()
            }
        }

    }


}