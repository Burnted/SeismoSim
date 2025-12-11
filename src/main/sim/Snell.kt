package main.sim

import main.geometry.Vec2
import kotlin.math.sqrt

object Snell {
    fun vecRefract(incident: Vec2, normal: Vec2, v1: Double, v2: Double): Vec2? {
        val nDotI = (normal * -1.0) dot incident
        val ratio = v1 / v2

        val root = 1.0 - ratio * ratio * (1.0 - nDotI * nDotI)
        if (root < 0) return null

        return normal * sqrt(root) + (incident - normal * nDotI) * ratio
    }

    fun reflect(incident: Vec2, normal: Vec2): Vec2 {
        return incident - normal * (2 * (incident dot normal))
    }
}