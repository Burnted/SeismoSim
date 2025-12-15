package main.presets

import main.geometry.Circle
import main.geometry.Vec2


object EarthPreset {
     const val TOTAL_RADIUS: Int = 6370

     const val CRUST_DV: Double      = 17.0 / 300.0
     const val LITHO_DV: Double      = 17.0 / 700.0
     const val ASTHENO_DV: Double    = 7.0 / 1500.0
     const val MANTLE_DV: Double     = 1.0 / 600.0
     const val OUT_DV: Double        = 1.0 / 2210.0

     const val CRUST_VI: Double      = 5.3
     const val LITHO_VI: Double      = 7.0
     const val ASTHENO_VI: Double    = 7.8
     const val MANTLE_VI: Double     = 8.2
     const val OUT_VI: Double        = 7.5
     const val IN_VI: Double         = 12.0
}