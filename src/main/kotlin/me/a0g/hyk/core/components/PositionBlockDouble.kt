package me.a0g.hyk.core.components

import net.minecraft.util.BlockPos
import kotlin.math.abs

class PositionBlockDouble(var x: Double, var y: Double, var z: Double){

    constructor(blockPos: BlockPos) : this(x = blockPos.x.toDouble(), y = blockPos.y.toDouble(), z = blockPos.z.toDouble())

    constructor(string: String) : this(x = string.split(",")[0].toDouble(), y = string.split(",")[1].toDouble(), z = string.split(",")[2].toDouble())


    fun add(x: Double, y: Double, z: Double): PositionBlockDouble {
        this.x = this.x + x
        this.y = this.y + y
        this.z = this.z + z

        return PositionBlockDouble(this.x, this.y, this.z)
    }

    fun toBlockPos(): BlockPos{
        return BlockPos(x,y,z)
    }

    fun toRoundedCenter(): PositionBlockDouble{
        //124.783 4 -108.077 -> need 124.5 4 -108.5
        val roundedX = getClosestTo(x)
        val roundedY = y
        val roundedZ = getClosestTo(z)

        return PositionBlockDouble(roundedX,roundedY,roundedZ)
    }

    fun toBlockPosRounded(): BlockPos{
        //124.783 4 -108.077 -> need 124.5 4 -108.5
        val roundedX = x.toInt()
        val roundedY = y.toInt()
        val roundedZ = z.toInt()

        return BlockPos(roundedX,roundedY,roundedZ)
    }

    fun toBlockPosRoundedCenter(): BlockPos{
        //124.783 4 -108.077 -> need 124.5 4 -108.5
        val roundedX = getClosestTo(x)
        val roundedY = y
        val roundedZ = getClosestTo(z)

        return BlockPos(roundedX,roundedY,roundedZ)
    }

    private fun getClosestTo(value: Double):Double{ //124.783
        val intValue = value.toInt() //124
        val minValue = intValue - 0.5 //123.5
        val midValue = intValue + 0.5 //124.5
        val maxValue = intValue + 1.5 //125.5
        var closest = 99999.0
        var closestValue = 0.0
        if( abs(value - minValue) < closest){
            closest = abs(value - minValue)
            closestValue = minValue
        } // 1.283 -> closest = 1.283
        if( abs(value - midValue) < closest) {
            closest = abs(value - midValue)
            closestValue = midValue
        } //0.283 -> closest =
        if( abs(value - maxValue) < closest){
            closest = abs(value -  maxValue)
            closestValue = maxValue
        }


        return closestValue
    }

    override fun toString(): String{
        return "$x,$y,$z"
    }

}
