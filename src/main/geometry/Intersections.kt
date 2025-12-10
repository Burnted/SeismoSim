package main.geometry

import kotlin.math.sqrt

object Intersections {

    fun rayCircleIntersection(ray: Ray, circle: Circle): Intersection? {
        val o = ray.origin
        val d = ray.direction

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

        val hit = o + d * t
        val normal = circle.normalAt(hit)

        return Intersection(hit, normal, t, circle)
    }
}