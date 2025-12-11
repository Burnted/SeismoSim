package main.geometry

import java.awt.Polygon


object EarthPreset {
    private const val TOTAL_RADIUS: Double = 6370.0

    private const val CRUST_DV: Double      = 17.0 / 300.0
    private const val LITHO_DV: Double      = 17.0 / 700.0
    private const val ASTHENO_DV: Double    = 7.0 / 1500.0
    private const val MANTLE_DV: Double     = 1.0 / 600.0
    private const val OUT_DV: Double        = 21.0 / 22100.0

    private const val CRUST_VI: Double      = 5.3
    private const val LITHO_VI: Double      = 7.0
    private const val ASTHENO_VI: Double    = 7.8
    private const val MANTLE_VI: Double     = 8.2
    private const val OUT_VI: Double        = 8.0
    private const val IN_VI: Double         = 12.0

    private const val SCALE:Double = 20.0

    fun createEarth(): MutableList<Circle> {
        val earth = mutableListOf<Circle>()
        earth.addAll(initCrust())
        earth.addAll(initLitho())
        earth.addAll(initAstheno())
        earth.addAll(initMantle())
        earth.addAll(initOut())
        earth.addAll(initIn())

        return earth
    }

    private fun initCrust(): MutableList<Circle> {
        val crust = mutableListOf<Circle>()
        for (depth in 0..30){
            crust.add(loopHelper(CRUST_DV, CRUST_VI, depth))
        }
        return crust
    }

    private fun initLitho(): MutableList<Circle>{
        val litho = mutableListOf<Circle>()
        for (depth in 31..100){
            litho.add(loopHelper(LITHO_DV, LITHO_VI, depth))
        }
        return litho
    }

    private fun initAstheno(): MutableList<Circle>{
        val astheno = mutableListOf<Circle>()
        for (depth in 101..250){
            astheno.add(loopHelper(ASTHENO_DV, ASTHENO_VI, depth))
        }
        return astheno
    }

    private fun initMantle(): MutableList<Circle>{
        val mantle = mutableListOf<Circle>()
        for (depth in 251..2891){
            mantle.add(loopHelper(MANTLE_DV, MANTLE_VI, depth))
        }
        return mantle
    }

    private fun initOut(): MutableList<Circle>{
        val outerCore = mutableListOf<Circle>()
        for (depth in 2891..5101){
            outerCore.add(loopHelper(OUT_DV, OUT_VI, depth))
        }
        return outerCore
    }

    private fun initIn(): MutableList<Circle>{
        val innerCore = mutableListOf<Circle>()
        for (depth in 5101..6370){
            innerCore.add(loopHelper(0.0, IN_VI, depth))
        }
        return innerCore
    }

    private fun loopHelper(dv: Double, vi: Double, depth: Int): Circle{
        val v = vi + depth.toDouble() * dv
        val r = (TOTAL_RADIUS - depth) / SCALE
        return Circle(radius = r, waveVelocity = v, layerDepth = depth)
    }
}