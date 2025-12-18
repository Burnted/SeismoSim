package main.sim

import main.geometry.Vec2
import kotlin.math.sqrt
import kotlin.math.max
import kotlin.math.min

object Snell {
    private val tempRefr = Vec2(0.0, 0.0)
    private val tempRefl = Vec2(0.0, 0.0)

    fun vecRefract(incident: Vec2, normal: Vec2, v1: Float, v2: Float): Vec2? {
        val iX = incident.x
        val iY = incident.y
        val iLen = sqrt(iX * iX + iY * iY)
        val iNormX = iX / iLen
        val iNormY = iY / iLen

        val nX = normal.x
        val nY = normal.y
        val nLen = sqrt(nX * nX + nY * nY)
        val nNormX = nX / nLen
        val nNormY = nY / nLen

        var cosi = -(nNormX * iNormX + nNormY * iNormY)
        cosi = max(-1.0, min(1.0, cosi))

        val eta = (v2 / v1).toDouble()
        val k = 1.0 - eta * eta * (1.0 - cosi * cosi)
        if (k < 0.0) return null

        val sqrtK = sqrt(k)
        val refractX = iNormX * eta + nNormX * (eta * cosi - sqrtK)
        val refractY = iNormY * eta + nNormY * (eta * cosi - sqrtK)

        val rLen = sqrt(refractX * refractX + refractY * refractY)
        tempRefr.x = refractX / rLen
        tempRefr.y = refractY / rLen
        return tempRefr
    }

    fun reflect(incident: Vec2, normal: Vec2): Vec2 {
        val iX = incident.x
        val iY = incident.y
        val iLen = sqrt(iX * iX + iY * iY)
        val iNormX = iX / iLen
        val iNormY = iY / iLen

        val nX = normal.x
        val nY = normal.y
        val nLen = sqrt(nX * nX + nY * nY)
        val nNormX = nX / nLen
        val nNormY = nY / nLen

        val dotProd = 2.0 * (iNormX * nNormX + iNormY * nNormY)
        val reflectX = iNormX - nNormX * dotProd
        val reflectY = iNormY - nNormY * dotProd

        val rLen = sqrt(reflectX * reflectX + reflectY * reflectY)
        tempRefl.x = reflectX / rLen
        tempRefl.y = reflectY / rLen
        return tempRefl
    }
}
