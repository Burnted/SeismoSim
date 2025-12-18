package main.sim

import main.geometry.*
import main.sim.Snell.reflect
import main.sim.Snell.vecRefract
import kotlin.math.sqrt

class RayTracer(circles: List<Circle>, private var ambientVelocity: Float, private val maxDepth: Int = 5) {
    private val circlesSorted: List<Circle> = circles.sortedByDescending { it.radius.toDouble() }
    private val radii: DoubleArray = circlesSorted.map { it.radius.toDouble() }.toDoubleArray()
    private val circleIndexMap: Map<Circle, Int> = circlesSorted.withIndex().associate { it.value to it.index }

    /**
     * Trace `initial` and append ray segments to `out`. Caller should provide a mutable list to reuse across frames.
     * This function minimizes temporary allocations by reusing Vec2s for computations and the working Ray.
     */
    fun trace(initial: Ray, out: MutableList<Ray>) {

        // working copy of the current ray (reused)
        val currentRay = Ray(Vec2(initial.origin.x, initial.origin.y), Vec2(initial.direction.x, initial.direction.y), Vec2(0.0, 0.0), initial.waveType)
        currentRay.direction.normalizeInPlace()
        currentRay.setEndDistance(1_000_000.0)

        fun layerForPoint(p: Vec2): Int {
            val d = sqrt(p.x * p.x + p.y * p.y)
            return (radii[0] - d).toInt()
        }

        var currentLayer = layerForPoint(currentRay.origin)
        var currentMediumVelo = if (currentLayer >= 0) circlesSorted[currentLayer].pWaveVelocity else ambientVelocity

        // temporary vector for nudged origin
        val tinyOriginTemp = Vec2(0.0, 0.0)

        for (depth in 0..<maxDepth) {
            val hit = findClosestIntersectionLimited(currentRay, currentLayer) ?: break

            // record the segment (create a copy for rendering)
            out.add(currentRay.copyForRecord().also { it.end.x = hit.point.x; it.end.y = hit.point.y })

            val circle = hit.circle
            val normal = circle.normalAt(hit.point)
            val entering = currentRay.direction.dot(normal) < 0.0

            if (circle.mediumType >= 4 && currentRay.waveType == 1) {
                // Absorb the S-wave
                break
            }

            val circleIdx = circleIndexMap[circle] ?: -1

            val v1: Float
            val v2: Float
            if (entering) {
                v1 = currentMediumVelo
                v2 = circle.pWaveVelocity
            } else {
                v1 = currentMediumVelo
                v2 = if (circleIdx - 1 >= 0) circlesSorted[circleIdx - 1].pWaveVelocity else ambientVelocity
            }

            val refractedDir = vecRefract(currentRay.direction, if (entering) normal else normal * -1.0, v1, v2)
            val newDir = if (refractedDir != null) {
                currentMediumVelo = v2
                refractedDir
            } else {
                reflect(currentRay.direction, normal * -1.0)
            }

            // normalize and nudge origin forward a tiny amount to avoid self-intersection
            val newDirNorm = newDir.normalizedCopy()
            tinyOriginTemp.x = hit.point.x + newDirNorm.x * 1e-4
            tinyOriginTemp.y = hit.point.y + newDirNorm.y * 1e-4

            currentRay.reset(tinyOriginTemp, newDir, 1_000_000.0)
            currentLayer = layerForPoint(currentRay.origin)
        }
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
