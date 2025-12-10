package main.sim

import main.geometry.Ray
import main.geometry.Vec2
import kotlin.math.sqrt

object Snell {
    fun refract(incident: Vec2, normal: Vec2, v1: Double, v2: Double): Vec2? {
        val cosI = -(normal.dot(incident))
        val ratio = v1 / v2

        val sinT2 = ratio * ratio * (1.0 - cosI * cosI)

        if (sinT2 > 1.0)
            return null // total internal reflection

        val cosT = sqrt(1.0 - sinT2)

        return incident * ratio + normal * (ratio * cosI - cosT)
    }

    fun reflect(incident: Vec2, normal: Vec2): Vec2 {
        val n = normal
        val d = incident
        return d - n * (2 * (d dot n))
    }
}