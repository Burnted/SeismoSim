package main.presets

import main.geometry.Circle
import main.geometry.Vec2


object EarthPreset {
    private const val TOTAL_RADIUS: Int = 6370

    private const val CRUST_DV: Double      = 17.0 / 300.0
    private const val LITHO_DV: Double      = 17.0 / 700.0
    private const val ASTHENO_DV: Double    = 7.0 / 1500.0
    private const val MANTLE_DV: Double     = 1.0 / 600.0
    private const val OUT_DV: Double        = 1.0 / 2210.0

    private const val CRUST_VI: Double      = 5.3
    private const val LITHO_VI: Double      = 7.0
    private const val ASTHENO_VI: Double    = 7.8
    private const val MANTLE_VI: Double     = 8.2
    private const val OUT_VI: Double        = 7.5
    private const val IN_VI: Double         = 12.0

    fun createEarth(): MutableList<Circle> {
        val earth = mutableListOf<Circle>()
        val sharedCenter = Vec2(0.0, 0.0)

        for (depth in 0..TOTAL_RADIUS) {
            val (dv, vi) = when (depth) {
                in 0..30 -> CRUST_DV to CRUST_VI
                in 31..100 -> LITHO_DV to LITHO_VI
                in 101..250 -> ASTHENO_DV to ASTHENO_VI
                in 251..2891 -> MANTLE_DV to MANTLE_VI
                in 2891..5101 -> OUT_DV to OUT_VI
                else -> 0.0 to IN_VI
            }
            val v = vi + depth * dv
            val r = (TOTAL_RADIUS - depth).toFloat()
            val circle = Circle(center = sharedCenter, radius = r, waveVelocity = v)
            earth.add(circle)
        }

        return earth
    }
}