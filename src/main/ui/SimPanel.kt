package main.ui

import main.geometry.Circle
import main.geometry.Ray
import main.geometry.Vec2
import main.presets.PresetReader
import main.presets.Presets
import main.render.Renderer
import main.sim.RayTracer
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel

class SimPanel : JPanel() {
    companion object {
        const val WIDTH = 1200
        const val HEIGHT = 1000
        const val STANDARD_RADIUS = 400f
        val center = Vec2(WIDTH / 2.0, HEIGHT / 2.0)
    }

    private val presetReader = PresetReader("earth.txt")
    private var preset: Pair<MutableList<Circle>, Float> = presetReader.readContentIntoCircles()
    private var circles = preset.first
    private var tracer: RayTracer = RayTracer(circles, maxDepth = preset.second.toInt() * 2)
    private var drawScale = STANDARD_RADIUS / preset.second

    private var waveType = 0

    var initialRayOrigin = Vec2(400.0, 400.0)
    private var rayDirs = mutableListOf<Vec2>()

    private lateinit var circleLayer: BufferedImage
    private var raysLayer: BufferedImage? = null
    private val raysBuffer = mutableListOf<Ray>()

    // Cache for ray rendering to avoid per-frame recomputation
    private var cachedRayOrigin = Vec2(-1e6, -1e6)
    private var cachedWaveType = -1
    private var cachedRayDirsHash = 0
    private var needsRayUpdate = true

    init {
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        initCircleLayer()
        updateRayCount(21)

        this.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                val x = (e.x.toDouble() - center.x) / drawScale
                val y = -(e.y.toDouble() - center.y) / drawScale
                initialRayOrigin.x = x
                initialRayOrigin.y = y
                needsRayUpdate = true
                repaint()
            }
        })
    }

    private fun initCircleLayer() {
        circleLayer = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)
        val g2d = circleLayer.createGraphics()
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            for (circle in circles) {
                val color = if (circle.mediumType % 2 == 0) {
                    Color(200, 200, 255)
                } else {
                    Color(150, 150, 255)
                }
                Renderer.drawCircle(g2d, circle, center, drawScale, color)
            }
        } finally {
            g2d.dispose()
        }
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        g2d.drawImage(circleLayer, 0, 0, null)

        if (raysLayer == null || raysLayer!!.width != WIDTH || raysLayer!!.height != HEIGHT) {
            raysLayer = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)
        }

        val buf = raysLayer!!
        val rg = buf.createGraphics()
        try {
            rg.composite = AlphaComposite.Clear
            rg.fillRect(0, 0, WIDTH, HEIGHT)
            rg.composite = AlphaComposite.SrcOver
            rg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            rg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED)

            // Only recompute rays if origin, direction count, or wave type changed
            if (needsRayUpdate ||
                cachedRayOrigin.x != initialRayOrigin.x ||
                cachedRayOrigin.y != initialRayOrigin.y ||
                cachedWaveType != waveType ||
                cachedRayDirsHash != rayDirs.hashCode()) {

                raysBuffer.clear()
                for (dir in rayDirs) {
                    val initial = Ray(initialRayOrigin.x, initialRayOrigin.y, dir.x, dir.y, waveType)
                    tracer.trace(initial, raysBuffer)
                }

                cachedRayOrigin.x = initialRayOrigin.x
                cachedRayOrigin.y = initialRayOrigin.y
                cachedWaveType = waveType
                cachedRayDirsHash = rayDirs.hashCode()
                needsRayUpdate = false
            }

            Renderer.drawRays(rg, raysBuffer, center, drawScale, Color.RED, 1.2f)
        } finally {
            rg.dispose()
        }

        g2d.drawImage(buf, 0, 0, null)
    }

    fun loadPreset(selectedPreset: String) {
        presetReader.fileName = if (selectedPreset == "custom") "simple.txt" else "$selectedPreset.txt"
        preset = presetReader.readContentIntoCircles()
        circles = preset.first
        tracer = RayTracer(circles, maxDepth = preset.second.toInt() * 2)
        drawScale = STANDARD_RADIUS / preset.second
        initCircleLayer()
        needsRayUpdate = true
        repaint()
    }

    fun updateRayCount(rayCount: Int) {
        rayDirs.clear()
        val step = 1.0 / rayCount
        for (i in 0..<rayCount) {
            val y = -0.5 + i * step
            rayDirs.add(Vec2(-1.0, y))
        }
        needsRayUpdate = true
        repaint()
    }

    fun updateVelocities(pIV: List<Float>, pFV: List<Float>, sIV: List<Float>, sFV: List<Float>) {
        circles = Presets.createSimple(pIV, pFV, sIV, sFV).first
        tracer = RayTracer(circles, maxDepth = preset.second.toInt() * 2)
        needsRayUpdate = true
        repaint()
    }

    fun updateWaveType(sWaveEnabled: Boolean) {
        waveType = if (sWaveEnabled) 1 else 0
        needsRayUpdate = true
        repaint()
    }
}
