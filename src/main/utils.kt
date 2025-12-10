package main
import main.geometry.Vec2
import kotlin.math.*

fun sgn(num: Double): Double {
    if (num < 0.0) return -1.0
    return 1.0
}

fun refract(d: Vec2, n: Vec2, n1: Double, n2: Double): Vec2? {
    val dir = d.normalized()
    val normal = n.normalized()
    val eta = n1 / n2

    val cosI = -(dir.dot(normal))
    val k = 1 - eta*eta * (1 - cosI*cosI)

    if (k < 0) return null // total internal reflection

    return dir * eta + normal * (eta * cosI - sqrt(k))
}
