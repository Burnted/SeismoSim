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

        // Pre-compute deltas to avoid repeated computation
        val pDeltas = FloatArray(4) { i -> (pIV[i] - pFV[i]) / arrayOf(100f, 75f, 125f, 100f)[i] }
        val sDeltas = FloatArray(4) { i -> (sIV[i] - sFV[i]) / arrayOf(100f, 75f, 125f, 100f)[i] }

        val ranges = arrayOf(Pair(0, 100), Pair(101, 175), Pair(176, 300), Pair(301, 400))

        for (depth in 0..400) {
            val (mediumIdx, rangeStart, rangeEnd) = when (depth) {
                in 0..100       -> Triple(0, ranges[0].first, ranges[0].second)
                in 101..175     -> Triple(1, ranges[1].first, ranges[1].second)
                in 176..300     -> Triple(2, ranges[2].first, ranges[2].second)
                else            -> Triple(3, ranges[3].first, ranges[3].second)
            }

            val pV = pIV[mediumIdx] + depth * pDeltas[mediumIdx]
            val sV = sIV[mediumIdx] + depth * sDeltas[mediumIdx]
            val r = (400 - depth).toFloat()
            val circle = Circle(center = sharedCenter, radius = r, pWaveVelocity = pV, sWaveVelocity = sV, mediumType = mediumIdx)
            simple.add(circle)
        }
        return simple to 400f
    }
}