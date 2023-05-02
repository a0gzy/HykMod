package me.a0g.hyk.core.components

class PositionDouble(var x: Double, var y: Double,var scale: Double){

    constructor(all: String) : this(fromString(all).x, fromString(all).y, fromString(all).scale )

    constructor(x: Float, y: Float, scale: Float) : this(x.toDouble(),y.toDouble(),scale.toDouble() )

    override fun toString(): String{
        return "$x,$y,$scale"
    }

    companion object {
        fun fromString(s: String): PositionDouble{
            val arr = s.split(",")
            return PositionDouble(arr[0].toDouble(),arr[1].toDouble(),arr[2].toDouble())
        }
    }
}

