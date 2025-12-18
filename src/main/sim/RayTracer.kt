package main.sim

import main.geometry.*
import main.sim.Snell.reflect
import main.sim.Snell.vecRefract
import kotlin.math.sqrt

class RayTracer(circles: List<Circle>, private val maxDepth: Int = 5) {
    private val circlesSorted: Array<Circle> = circles.sortedByDescending { it.radius.toDouble() }.toTypedArray()
    private val radii: DoubleArray = circlesSorted.map { it.radius.toDouble() }.toDoubleArray()
    private val pVelocities: FloatArray = FloatArray(circlesSorted.size) { i -> circlesSorted[i].pWaveVelocity }
    private val sVelocities: FloatArray = FloatArray(circlesSorted.size) { i -> circlesSorted[i].sWaveVelocity }
    private val circlesCount: Int = circlesSorted.size

    private val pAmbientVel: Float = circlesSorted.first().pWaveVelocity
    private val sAmbientVel: Float = circlesSorted.first().sWaveVelocity
    private val maxRadius: Double = radii[0]

    // Pre-allocated temp vectors
    private val tempNormal = Vec2(0.0, 0.0)
    private val tinyOriginTemp = Vec2(0.0, 0.0)
    private val tempHitPoint = Vec2(0.0, 0.0)
    private val tempNegNormal = Vec2(0.0, 0.0)
    private val tempDir = Vec2(0.0, 0.0)

    fun trace(initial: Ray, out: MutableList<Ray>) {
        val currentRay = Ray(Vec2(initial.origin.x, initial.origin.y), Vec2(initial.direction.x, initial.direction.y), Vec2(0.0, 0.0), initial.waveType)
        currentRay.direction.normalizeInPlace()
        currentRay.setEndDistance(1_000_000.0)

        var currentLayer = layerForPoint(currentRay.origin)
        var currentMediumVelo = getVelocity(currentLayer, currentRay.waveType)
        var bestT = Double.MAX_VALUE
        var bestCircleIdx = -1

        for (depth in 0..<maxDepth) {
            bestT = Double.MAX_VALUE
            bestCircleIdx = -1

            val oX = currentRay.origin.x
            val oY = currentRay.origin.y
            val dX = currentRay.direction.x
            val dY = currentRay.direction.y

            // Search in local range only
            val low: Int
            val high: Int
            if (currentLayer in 0..<circlesCount) {
                low = (currentLayer - 1).coerceAtLeast(0)
                high = (currentLayer + 1).coerceAtMost(circlesCount - 1)
            } else {
                low = 0
                high = if (circlesCount > 1) 1 else 0
            }

            // Inline ray-circle intersection test
            for (i in high downTo low) {
                val circle = circlesSorted[i]
                val cX = circle.center.x
                val cY = circle.center.y
                val r = circle.radius.toDouble()

                val fX = oX - cX
                val fY = oY - cY

                val b = 2.0 * (fX * dX + fY * dY)
                val cTerm = fX * fX + fY * fY - r * r

                val discriminant = b * b - 4.0 * cTerm
                if (discriminant < 0.0) continue

                val sqrtD = sqrt(discriminant)
                val t1 = (-b - sqrtD) * 0.5
                val t2 = (-b + sqrtD) * 0.5

                val t = if (t1 >= 1e-12) t1 else if (t2 >= 1e-12) t2 else continue

                if (t < bestT) {
                    bestT = t
                    bestCircleIdx = i
                }
            }

            if (bestCircleIdx < 0) break

            val circle = circlesSorted[bestCircleIdx]
            tempHitPoint.x = oX + dX * bestT
            tempHitPoint.y = oY + dY * bestT

            // Record ray
            out.add(Ray(
                Vec2(oX, oY),
                Vec2(dX, dY),
                Vec2(tempHitPoint.x, tempHitPoint.y),
                currentRay.waveType
            ))

            // Calculate normal (inline)
            val nx = tempHitPoint.x - circle.center.x
            val ny = tempHitPoint.y - circle.center.y
            val nLen = sqrt(nx * nx + ny * ny)
            tempNormal.set(nx / nLen, ny / nLen)

            val entering = currentRay.direction.dot(tempNormal) < 0.0

            // Get velocities
            val v1: Float
            val v2: Float
            if (entering) {
                v1 = currentMediumVelo
                v2 = if (currentRay.waveType == 0) circle.pWaveVelocity else circle.sWaveVelocity
            } else {
                v1 = currentMediumVelo
                v2 = if (bestCircleIdx > 0) {
                    if (currentRay.waveType == 0) pVelocities[bestCircleIdx - 1] else sVelocities[bestCircleIdx - 1]
                } else {
                    if (currentRay.waveType == 0) pAmbientVel else sAmbientVel
                }
            }

            // Refract or reflect
            if (entering) {
                val refracted = vecRefract(currentRay.direction, tempNormal, v1, v2)
                if (refracted != null) {
                    currentMediumVelo = v2
                    tempDir.set(refracted.x, refracted.y)
                } else {
                    reflect(currentRay.direction, tempNormal, tempDir)
                }
            } else {
                tempNegNormal.set(-tempNormal.x, -tempNormal.y)
                val refracted = vecRefract(currentRay.direction, tempNegNormal, v1, v2)
                if (refracted != null) {
                    currentMediumVelo = v2
                    tempDir.set(refracted.x, refracted.y)
                } else {
                    reflect(currentRay.direction, tempNegNormal, tempDir)
                }
            }

            val newDirNorm = tempDir.normalizedCopy()
            tinyOriginTemp.set(tempHitPoint.x + newDirNorm.x * 1e-4, tempHitPoint.y + newDirNorm.y * 1e-4)

            currentRay.reset(tinyOriginTemp, tempDir, 1_000_000.0)
            currentLayer = layerForPoint(currentRay.origin)
        }
    }

    private inline fun layerForPoint(p: Vec2): Int {
        val d = sqrt(p.x * p.x + p.y * p.y)
        return (maxRadius - d).toInt()
    }

    private inline fun getVelocity(layer: Int, waveType: Int): Float {
        return if (layer in 0..<circlesCount) {
            if (waveType == 0) pVelocities[layer] else sVelocities[layer]
        } else {
            if (waveType == 0) pAmbientVel else sAmbientVel
        }
    }
}
