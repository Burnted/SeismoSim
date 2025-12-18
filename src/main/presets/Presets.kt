package main.presets

import main.geometry.Circle
import main.geometry.Vec2

object Presets {
    fun createSimple(pIV: List<Float> = listOf(12f, 15f, 9f, 12f),
                     pFV: List<Float> = listOf(12f, 15f, 9f, 12f),
                     sIV: List<Float> = listOf(6f, 7.5f, 0f, 0f),
                     sFV: List<Float>  = listOf(6f, 7.5f, 0f, 0f)
    ): Pair<MutableList<Circle>, Float> {
        val simple = mutableListOf<Circle>()
        val sharedCenter = Vec2(0.0, 0.0)
        val pDV0 = (pIV[0] - pFV[0]) / 100f
        val pDV1 = (pIV[1] - pFV[1]) / 75f
        val pDV2 = (pIV[2] - pFV[2]) / 125f
        val pDV3 = (pIV[3] - pFV[3]) / 100f

        val sDV0 = (sIV[0] - sFV[0]) / 100f
        val sDV1 = (sIV[1] - sFV[1]) / 75f
        val sDV2 = (sIV[2] - sFV[2]) / 125f
        val sDV3 = (sIV[3] - sFV[3]) / 100f

        for (depth in 0..400) {
            val (p, s, mediumNum) = when (depth) {
                in 0..100       -> Triple(pDV0 to pIV[0], sDV0 to sIV[0], 0)
                in 101..175     -> Triple(pDV1 to pIV[1], sDV1 to sIV[1], 1)
                in 176..300     -> Triple(pDV2 to pIV[2], sDV2 to sIV[2], 2)
                else                 -> Triple(pDV3 to pIV[3], sDV3 to sIV[3], 3)
            }

            val pV = p.second + depth * p.first
            val sV = s.second + depth * s.first
            val r = (400 - depth).toFloat()
            val circle = Circle(center = sharedCenter, radius = r, pWaveVelocity = pV, sWaveVelocity = sV, mediumType = mediumNum)
            simple.add(circle)
        }
        return simple to 400f
    }
}