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
            println("radius: $r, velocity: $v, medium: $mediumNum")
            val circle = Circle(center = sharedCenter, radius = r, pWaveVelocity = v, mediumType = mediumNum)
            earth.add(circle)
        }

        return earth to 6370f
    }

    fun createSimple(vI0: Float = 12f, vF0: Float = 12f,
                     vI1: Float = 15f, vF1: Float = 15f,
                     vI2: Float = 9f, vF2: Float = 9f,
                     vI3: Float = 12f, vF3: Float = 12f
    ): Pair<MutableList<Circle>, Float> {
        val simple = mutableListOf<Circle>()
        val sharedCenter = Vec2(0.0, 0.0)
        val dv0 = (vF0 - vI0) / 100f
        val dv1 = (vF1 - vI1) / 75f
        val dv2 = (vF2 - vI2) / 125f
        val dv3 = (vF3 - vI3) / 100f

        for (depth in 0..400) {
            val (dv, vi, mediumNum) = when (depth) {
                in 0..100 -> Triple(dv0, vI0, 0)
                in 101..175 -> Triple(dv1, vI1, 1)
                in 176..300 -> Triple(dv2, vI2, 2)
                else -> Triple(dv3, vI3, 3)
            }

            val v = vi + depth * dv
            val r = (400 - depth).toFloat()
            val circle = Circle(center = sharedCenter, radius = r, pWaveVelocity = v, mediumType = mediumNum)
            simple.add(circle)
        }
        return simple to 400f
    }
}