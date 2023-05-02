package me.a0g.hyk.core.features.client

import me.a0g.hyk.HykX
import me.a0g.hyk.utils.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object WaypointPath {

    // 1: ( { 1, (+52,+132,-362) }, { 2, (+52,+132,-362) }, { 3, (+52,+132,-362) } )
    var pathsArray: MutableMap<Int, MutableMap<Int,BlockPos>> = mutableMapOf()

    @SubscribeEvent
    fun onWorldRender(event: RenderWorldLastEvent) {
        if (HykX.config.path && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            if(pathsArray.isNotEmpty()){
                for(path in pathsArray){
                    val id = path.key
                    val pathWaypoints = path.value
                    val keys = pathWaypoints.keys.sorted() // 0 1 2 3 4 7
                    for (key in keys.indices) {
                        val waypointId = keys[key]
                        val waypointPosition = pathWaypoints[waypointId]!!  // 1
                        RenderUtil.renderWaypointText(
                            "§6§l$id: §8$waypointId",
                            waypointPosition.x.toDouble(), waypointPosition.y.toDouble(), waypointPosition.z.toDouble(),
                            event.partialTicks, false
                        )

                        if (key > 0) {
                            val previousWaypointId = keys[key - 1]
                            val previousWaypointPosition = pathWaypoints[previousWaypointId]!!
                            RenderUtil.draw3DLine(
                                pos1 = Vec3(previousWaypointPosition), pos2 = Vec3(waypointPosition),
                                color = Color.WHITE, lineWidth = 1, depth = false, partialTicks = event.partialTicks
                            )
                        }
                    }
                }
            }
        }
    }
}