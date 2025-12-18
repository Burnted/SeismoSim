package main.sim

import main.geometry.*
import main.sim.Snell.reflect
import main.sim.Snell.vecRefract
import kotlin.math.sqrt

class RayTracer(circles: List<Circle>, private val maxDepth: Int = 5) {
    private val circlesSorted: List<Circle> = circles.sortedByDescending { it.radius.toDouble() }
    private val radii: DoubleArray = circlesSorted.map { it.radius.toDouble() }.toDoubleArray()
    private val circleIndexArray: IntArray = IntArray(circlesSorted.size) { it }

    private val pAmbientVel = circlesSorted.first().pWaveVelocity
    private val sAmbientVel = circlesSorted.first().sWaveVelocity

    private val pVelocities = FloatArray(circlesSorted.size) { i -> circlesSorted[i].pWaveVelocity }
    private val sVelocities = FloatArray(circlesSorted.size) { i -> circlesSorted[i].sWaveVelocity }

    private val tempNormal = Vec2(0.0, 0.0)
    private val tinyOriginTemp = Vec2(0.0, 0.0)

    fun trace(initial: Ray, out: MutableList<Ray>) {
        val currentRay = Ray(Vec2(initial.origin.x, initial.origin.y), Vec2(initial.direction.x, initial.direction.y), Vec2(0.0, 0.0), initial.waveType)
        currentRay.direction.normalizeInPlace()
        currentRay.setEndDistance(1_000_000.0)

        var currentLayer = layerForPoint(currentRay.origin)
        var currentMediumVelo = getVelocity(currentLayer, currentRay.waveType)

        for (depth in 0..<maxDepth) {
            val hit = findClosestIntersectionLimited(currentRay, currentLayer) ?: break

            out.add(currentRay.copyForRecord().also { it.end.x = hit.point.x; it.end.y = hit.point.y })

            val circle = hit.circle
            circle.normalAt(hit.point, tempNormal)
            val entering = currentRay.direction.dot(tempNormal) < 0.0

            // Binary search for circle index (faster than map lookup for sorted array)
            var circleIdx = -1
            for (i in circlesSorted.indices) {
                if (circlesSorted[i] === circle) {
                    circleIdx = i
                    break
                }
            }

            val v1: Float
            val v2: Float
            if (entering) {
                v1 = currentMediumVelo
                v2 = if (currentRay.waveType == 0) circle.pWaveVelocity else circle.sWaveVelocity
            } else {
                v1 = currentMediumVelo
                v2 = if (circleIdx - 1 >= 0) {
                    if (currentRay.waveType == 0) pVelocities[circleIdx - 1]
                    else sVelocities[circleIdx - 1]
                } else {
                    if (currentRay.waveType == 0) pAmbientVel else sAmbientVel
                }
            }

            val normalDir = if (entering) tempNormal else tempNormal * -1.0
            val refractedDir = vecRefract(currentRay.direction, normalDir, v1, v2)
            val newDir = if (refractedDir != null) {
                currentMediumVelo = v2
                refractedDir
            } else {
                reflect(currentRay.direction, normalDir)
            }

            val newDirNorm = newDir.normalizedCopy()
            tinyOriginTemp.x = hit.point.x + newDirNorm.x * 1e-4
            tinyOriginTemp.y = hit.point.y + newDirNorm.y * 1e-4

            currentRay.reset(tinyOriginTemp, newDir, 1_000_000.0)
            currentLayer = layerForPoint(currentRay.origin)
        }
    }

    private fun layerForPoint(p: Vec2): Int {
        val d = sqrt(p.x * p.x + p.y * p.y)
        return (radii[0] - d).toInt()
    }

    private fun getVelocity(layer: Int, waveType: Int): Float {
        return if (layer >= 0 && layer < circlesSorted.size) {
            if (waveType == 0) pVelocities[layer] else sVelocities[layer]
        } else {
            if (waveType == 0) pAmbientVel else sAmbientVel
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
