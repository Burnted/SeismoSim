package main.geometry

import kotlin.math.sqrt

object Intersections {

    fun rayCircleIntersection(ray: Ray, circle: Circle): Intersection? {
        val oX = ray.origin.x
        val oY = ray.origin.y
        val dX = ray.direction.x
        val dY = ray.direction.y

        val cX = circle.center.x
        val cY = circle.center.y
        val r = circle.radius.toDouble()

        val fX = oX - cX
        val fY = oY - cY

        val b = 2.0 * (fX * dX + fY * dY)
        val cTerm = fX * fX + fY * fY - r * r

        val discriminant = b * b - 4.0 * cTerm
        if (discriminant < 0.0) return null

        val sqrtD = sqrt(discriminant)
        val t1 = (-b - sqrtD) * 0.5
        val t2 = (-b + sqrtD) * 0.5

        val t = when {
            t1 >= 1e-12 -> t1
            t2 >= 1e-12 -> t2
            else -> return null
        }

        val hitX = oX + dX * t
        val hitY = oY + dY * t
        val hit = Vec2(hitX, hitY)

        val nx = hitX - cX
        val ny = hitY - cY
        val nLen = sqrt(nx * nx + ny * ny)
        val normal = Vec2(nx / nLen, ny / nLen)

        return Intersection(hit, normal, t, circle)
    }
}
