package main.sim

import main.geometry.*
import main.sim.Snell.reflect
import main.sim.Snell.refract

class RayTracer(
    private val circles: List<Circle>,
    private var ambientVelocity: Double,
    private val maxDepth: Int = 5
) {
    //TODO: add total internal reflection checks and allow pass through

    fun trace(initial: Ray): List<Ray> {
        val rayPath = mutableListOf<Ray>()
        var currentRay = initial
        var currentMediumVelo = ambientVelocity
        var dir: Vec2

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
            } else {
                // Inside → Outside
                v1 = currentMediumVelo
                v2 = ambientVelocity
                println("${currentRay.direction.dot(normal)}")
            }

            ambientVelocity = v1

            val refractedDir = refract(currentRay.direction, if (entering) normal else normal * -1.0, v1, v2)

            currentMediumVelo = v2 // update medium for next segment

            // If total internal reflection:
            dir = refractedDir ?: reflect(currentRay.direction, normal)

            // Refracted
            currentRay = Ray(hitPoint + dir.normalized()*0.001, dir, hitPoint + dir.normalized()*1000.0)
            //rayPath.add(currentRay)
        }

        return rayPath
    }

    private fun findClosestIntersection(ray: Ray): Intersection? {
        return circles.mapNotNull { Intersections.rayCircleIntersection(ray, it) }.minByOrNull { it.distance }
    }
}