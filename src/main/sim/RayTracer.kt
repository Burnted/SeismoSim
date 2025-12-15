package main.sim

import main.geometry.*
import main.sim.Snell.reflect
import main.sim.Snell.vecRefract
import kotlin.math.hypot

class RayTracer(
    circles: List<Circle>,
    private var ambientVelocity: Double,
    private val maxDepth: Int = 5
) {
    private val circlesSorted: List<Circle> = circles.sortedByDescending { it.radius.toDouble() }
    private val radii: DoubleArray = circlesSorted.map { it.radius.toDouble() }.toDoubleArray()

    fun trace(initial: Ray): List<Ray> {
        val rayPath = mutableListOf<Ray>()

        val currentRay = Ray(Vec2(initial.origin.x, initial.origin.y), Vec2(initial.direction.x, initial.direction.y), Vec2(0.0, 0.0))
        currentRay.direction.normalizeInPlace()
        currentRay.setEndDistance(1_000_000.0)

        fun layerForPoint(p: Vec2): Int {
            val d = hypot(p.x, p.y)
            for (i in radii.indices.reversed()) {
                if (d <= radii[i]) return i
            }
            return -1
        }

        var currentLayer = layerForPoint(currentRay.origin)

        var currentMediumVelo = if (currentLayer >= 0) circlesSorted[currentLayer].waveVelocity else ambientVelocity

        for (iDepth in 0..<maxDepth) {
            val hit = findClosestIntersectionLimited(currentRay, currentLayer) ?: break

            val hitPoint = hit.point
            rayPath.add(Ray(Vec2(currentRay.origin.x, currentRay.origin.y), Vec2(currentRay.direction.x, currentRay.direction.y), Vec2(hitPoint.x, hitPoint.y)))

            val circle = hit.circle
            val normal = circle.normalAt(hitPoint)
            val entering = currentRay.direction.dot(normal) < 0.0

            val v1: Double
            val v2: Double

            val circleIdx = circlesSorted.indexOf(circle)

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

    /**
     * For concentric layers, only test the immediate neighbor circles: the current layer's boundary,
     * the layer inside, and the layer outside (at most 3 tests).
     */
    private fun findClosestIntersectionLimited(ray: Ray, currentLayer: Int): Intersection? {
        val candidates = ArrayList<Circle>(3)
        if (currentLayer >= 0) {
            candidates.add(circlesSorted[currentLayer])
            if (currentLayer - 1 >= 0) candidates.add(circlesSorted[currentLayer - 1])
            if (currentLayer + 1 < circlesSorted.size) candidates.add(circlesSorted[currentLayer + 1])
        } else {
            val first = 0
            if (first < circlesSorted.size) {
                candidates.add(circlesSorted[first])
                if (first + 1 < circlesSorted.size) candidates.add(circlesSorted[first + 1])
            }
        }

        var best: Intersection? = null
        for (c in candidates) {
            val intr = Intersections.rayCircleIntersection(ray, c)
            if (intr != null) {
                if (best == null || intr.distance < best.distance) best = intr
            }
        }
        return best
    }
}
