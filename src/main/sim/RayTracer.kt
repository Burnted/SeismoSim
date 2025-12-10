package main.sim

import main.geometry.*

class RayTracer(
    private val circles: List<Circle>,
    private val v1: Double,
    private val v2: Double,
    private val maxDepth: Int = 5
) {
    fun trace(initial: Ray): List<Ray> {
        val rays = mutableListOf<Ray>()
        var current = initial

        repeat(maxDepth) {
            println("t")
            val hit = findClosestIntersection(current) ?: return rays
            rays.add(Ray(current.origin, current.direction, hit.point))
            println("w")

            val refractedDir = Snell.refract(current.direction, hit.normal, v1, v2) ?: return rays
            println("q")

            current = Ray(hit.point + refractedDir.normalized(), refractedDir.normalized(), hit.point + refractedDir.normalized() * 1000.0)
        }
        return rays
    }

    private fun findClosestIntersection(ray: Ray): Intersection? {
        return circles.mapNotNull { Intersections.rayCircleIntersection(ray, it) }.minByOrNull { it.distance }
    }
}