package main.sim

import main.geometry.*
import main.sim.Snell.reflect
import main.sim.Snell.vecRefract
import kotlin.math.sqrt

class RayTracer(circles: List<Circle>, private var ambientVelocity: Float, private val maxDepth: Int = 5) {
    private val circlesSorted: List<Circle> = circles.sortedByDescending { it.radius.toDouble() }
    private val radii: DoubleArray = circlesSorted.map { it.radius.toDouble() }.toDoubleArray()

    private val circleIndexMap: Map<Circle, Int> = circlesSorted.withIndex().associate { it.value to it.index }

    fun trace(initial: Ray): List<Ray> {
        val rayPath = mutableListOf<Ray>()

        val currentRay = Ray(Vec2(initial.origin.x, initial.origin.y), Vec2(initial.direction.x, initial.direction.y), Vec2(0.0, 0.0), initial.waveType)
        currentRay.direction.normalizeInPlace()
        currentRay.setEndDistance(1_000_000.0)

        fun layerForPoint(p: Vec2): Int {
            val d = sqrt(p.x * p.x + p.y * p.y)
            return (radii[0] - d).toInt()
        }

        var currentLayer = layerForPoint(currentRay.origin)

        var currentMediumVelo = if (currentLayer >= 0) circlesSorted[currentLayer].waveVelocity else ambientVelocity

        for (iDepth in 0..< maxDepth) {
            val hit = findClosestIntersectionLimited(currentRay, currentLayer) ?: break

            val hitPoint = hit.point
            rayPath.add(Ray(Vec2(currentRay.origin.x, currentRay.origin.y), Vec2(currentRay.direction.x, currentRay.direction.y), Vec2(hitPoint.x, hitPoint.y), currentRay.waveType))

            val circle = hit.circle
            val normal = circle.normalAt(hitPoint)
            val entering = currentRay.direction.dot(normal) < 0.0

            if (circle.mediumType >= 4 && currentRay.waveType == 1) {
                // Absorb the S-wave
                break
            }

            val v1: Float
            val v2: Float

            val circleIdx = circleIndexMap[circle] ?: -1

            if (entering) {
                v1 = currentMediumVelo
                v2 = circle.waveVelocity
            } else {
                v1 = currentMediumVelo
                v2 = if (circleIdx - 1 >= 0) circlesSorted[circleIdx - 1].waveVelocity else ambientVelocity
            }

            val refractedDir = vecRefract(currentRay.direction, if (entering) normal else normal * -1.0, v1, v2)

            val newDir = if (refractedDir != null) {
                currentMediumVelo = v2
                refractedDir
            } else {
                reflect(currentRay.direction, normal * -1.0)
            }

            // normalize once and reuse when nudging origin
            val newDirNorm = newDir.normalizedCopy()
            val tinyOrigin = Vec2(hitPoint.x + newDirNorm.x * 0.0001, hitPoint.y + newDirNorm.y * 0.0001)
            currentRay.reset(tinyOrigin, newDir, 1_000_000.0)

            currentLayer = layerForPoint(currentRay.origin)
        }

        return rayPath
    }


    private fun findClosestIntersectionLimited(ray: Ray, currentLayer: Int): Intersection? {
        val circles = circlesSorted
        val n = circles.size

        val low: Int
        val high: Int
        if (currentLayer >= 0) {
            low = (currentLayer - 1).coerceAtLeast(0)
            high = (currentLayer + 1).coerceAtMost(n - 1)
        } else {
            low = 0
            high = if (n > 1) 1 else 0
        }

        for (i in high downTo low) {
            val intr = Intersections.rayCircleIntersection(ray, circles[i])
            if (intr != null) {
                return intr
            }
        }

        return null
    }
}
