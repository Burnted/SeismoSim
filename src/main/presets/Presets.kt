package main.presets

import main.geometry.Circle
import main.geometry.Vec2

object Presets {
    fun createEarth(): Pair<MutableList<Circle>, Float> {
        val earth = mutableListOf<Circle>()
        val sharedCenter = Vec2(0.0, 0.0)

        for (depth in 0..EarthPreset.TOTAL_RADIUS) {
            val (dv, vi, mediumNum) = when (depth) {
                in 0..30 -> Triple(EarthPreset.CRUST_DV, EarthPreset.CRUST_VI, 0)
                in 31..100 -> Triple(EarthPreset.LITHO_DV, EarthPreset.LITHO_VI, 1)
                in 101..250 -> Triple(EarthPreset.ASTHENO_DV, EarthPreset.ASTHENO_VI, 2)
                in 251..2891 -> Triple(EarthPreset.MANTLE_DV, EarthPreset.MANTLE_VI, 3)
                in 2891..5101 -> Triple(EarthPreset.OUT_DV, EarthPreset.OUT_VI, 4)
                else -> Triple(0.0, EarthPreset.IN_VI, 5)
            }
            val v = vi + depth * dv
            val r = (EarthPreset.TOTAL_RADIUS - depth).toFloat()
            val circle = Circle(center = sharedCenter, radius = r, waveVelocity = v, mediumType = mediumNum)
            earth.add(circle)
        }

        return earth to 6370f
    }

    fun createSimple(): Pair<MutableList<Circle>, Float> {
        val mantle      = Circle(Vec2(0.0, 0.0), 306.0f, 12.0, 0)
        val midMantle   = Circle(Vec2(0.0, 0.0), 250.0f, 15.0, 1)
        val outerCore   = Circle(Vec2(0.0, 0.0), 174.0f, 9.0, 2)
        val innerCore   = Circle(Vec2(0.0, 0.0), 63.5f, 11.8, 3)
        return mutableListOf(mantle, midMantle, outerCore, innerCore) to 306f
    }
}