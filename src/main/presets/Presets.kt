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
                else -> Triple(0.0f, EarthPreset.IN_VI, 5)
            }

            val v = vi + depth * dv
            val r = (EarthPreset.TOTAL_RADIUS - depth).toFloat()
            val circle = Circle(center = sharedCenter, radius = r, waveVelocity = v, mediumType = mediumNum)
            earth.add(circle)
        }

        return earth to 6370f
    }

    fun createSimple(): Pair<MutableList<Circle>, Float> {
        val simple = mutableListOf<Circle>()
        val sharedCenter = Vec2(0.0, 0.0)

        for (depth in 0..400) {
            val (dv, vi, mediumNum) = when (depth) {
                in 0..100 -> Triple(0f, 12f, 0)
                in 101..175 -> Triple(0f, 15f, 1)
                in 176..300 -> Triple(0f, 9f, 2)
                else -> Triple(0f, 12f, 3)
            }

            val v = vi + depth * dv
            val r = (400 - depth).toFloat()
            val circle = Circle(center = sharedCenter, radius = r, waveVelocity = v, mediumType = mediumNum)
            simple.add(circle)
        }
        return simple to 400f
    }
}