package main.geometry

import kotlin.math.sqrt

object Intersections {

    fun rayCircle(ray: Ray, circle: Circle): Vec2? {
        val o = ray.origin
        val d = ray.dir

        val c = circle.center
        val r = circle.radius

        val f = o - c

        val a = d.dot(d)
        val b = 2.0 * f.dot(d)
        val cTerm = f.dot(f) - r * r

        val discriminant = b * b - 4 * a * cTerm
        if (discriminant < 0) return null

        val sqrtD = sqrt(discriminant)

        val t1 = (-b - sqrtD) / (2 * a)
        val t2 = (-b + sqrtD) / (2 * a)

        val t = when {
            t1 >= 0 -> t1
            t2 >= 0 -> t2
            else -> return null
        }

        return o + d * t
    }
}