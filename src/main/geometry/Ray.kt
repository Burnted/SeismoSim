package main.geometry

import kotlin.math.*

class Ray(var origin: Vec2, direction: Vec2) {

    var dir: Vec2 = direction.normalized()

    fun refract(normal: Vec2, n1: Double, n2: Double): Vec2 {
        val n = normal.normalized()
        val d = dir.normalized()

        val cosI = -(n dot d)
        val eta = n1 / n2
        val k = 1 - eta * eta * (1 - cosI * cosI)

        if (k < 0) {
            return reflect(normal)
        }

        return (d * eta) + (n * (eta * cosI - sqrt(k)))
    }

    fun reflect(normal: Vec2): Vec2 {
        val n = normal.normalized()
        val d = dir
        return d - n * (2 * (d dot n))
    }
}