package main.presets

import main.geometry.Circle
import main.geometry.Vec2

object SimplePreset {

    fun createSimple(): MutableList<Circle> {
         val mantle      = Circle(Vec2(0.0, 0.0), 306.0f, 12.0)
         val midMantle   = Circle(Vec2(0.0, 0.0), 250.0f, 15.0)
         val outerCore   = Circle(Vec2(0.0, 0.0), 174.0f, 9.0)
         val innerCore   = Circle(Vec2(0.0, 0.0), 63.5f, 11.8)
         return mutableListOf(mantle, midMantle, outerCore, innerCore)
    }
}