// kotlin
package main.sim

import main.geometry.Vec2
import kotlin.math.sqrt
import kotlin.math.max
import kotlin.math.min

object Snell {
    /**
     * Refract `incident` across surface with `normal`.
     * Uses velocities (v1 = current medium speed, v2 = next medium speed).
     * eta = n1 / n2 = (1/v1) / (1/v2) = v2 / v1.
     *
     * Returns null on total internal reflection.
     */
    fun vecRefract(incident: Vec2, normal: Vec2, v1: Double, v2: Double): Vec2? {
        val I = incident.normalizedCopy()
        val N = normal.normalizedCopy()

        // clamp cosi to [-1,1] for numeric stability
        var cosi = - (N dot I)
        cosi = max(-1.0, min(1.0, cosi))

        val eta = v2 / v1
        val k = 1.0 - eta * eta * (1.0 - cosi * cosi)
        if (k < 0.0) return null

        val refr = I * eta + N * (eta * cosi - sqrt(k))
        return refr.normalizedCopy()
    }

    fun reflect(incident: Vec2, normal: Vec2): Vec2 {
        val I = incident.normalizedCopy()
        val N = normal.normalizedCopy()
        val reflected = I - N * (2.0 * (I dot N))
        return reflected.normalizedCopy()
    }
}
