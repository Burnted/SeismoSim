package main.sim

import main.geometry.*
import main.sim.Snell.reflect
import main.sim.Snell.vecRefract

class RayTracer(
    private val circles: List<Circle>,
    private var ambientVelocity: Double,
    private val maxDepth: Int = 5
) {

    fun trace(initial: Ray): List<Ray> {
        val rayPath = mutableListOf<Ray>()
        var currentRay = initial
        var currentMediumVelo = ambientVelocity
        var dir: Vec2

        println()
        for(i in 0..maxDepth) {
            val hit = findClosestIntersection(currentRay)
                ?: return rayPath // Ray hits nothing → end trace

            val circle = hit.circle
            val hitPoint = hit.point
            rayPath.add(Ray(currentRay.origin, currentRay.direction, hitPoint))

            val normal = circle.normalAt(hitPoint)
            val entering = currentRay.direction.dot(normal) < 0

            val v1: Double
            val v2: Double

            if (entering) {
                v1 = currentMediumVelo
                v2 = circle.waveVelocity
                println("Entering circle: ${circle.layerDepth}, v1: $v1, v2: $v2")
            } else {
                // Inside → Outside
                v1 = currentMediumVelo
                v2 = if (circle.layerDepth > 0) {
                    circles[circle.layerDepth - 1].waveVelocity
                } else ambientVelocity
                println("exiting circle: ${circle.layerDepth}, v1: $v1, v2: $v2")
            }

            val refractedDir = vecRefract(currentRay.direction, if (entering) normal else normal * -1.0, v1, v2)

            if (refractedDir != null) {
                dir = refractedDir
                currentMediumVelo = v2
            } else  {
                dir = reflect(currentRay.direction,normal * -1.0)
            }

            currentRay = Ray(hitPoint + dir.normalized()*0.0001, dir, hitPoint + dir.normalized()*1000.0)
        }

        return rayPath
    }

    private fun findClosestIntersection(ray: Ray): Intersection? {
        return circles.mapNotNull { Intersections.rayCircleIntersection(ray, it) }.minByOrNull { it.distance }
    }
}