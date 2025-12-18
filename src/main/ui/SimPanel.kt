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
    private var tracer: RayTracer = RayTracer(circles, ambientVelocity = 5.3f, maxDepth = preset.second.toInt() * 2)
    private var drawScale = STANDARD_RADIUS / preset.second

    var initialRayOrigin = Vec2(400.0, 400.0)

    private var rayDirs = mutableListOf(
        Vec2(-1.0, 0.5),
        Vec2(-1.0, 0.45),
        Vec2(-1.0, 0.4),
        Vec2(-1.0, 0.35),
        Vec2(-1.0, 0.3),
        Vec2(-1.0, 0.25),
        Vec2(-1.0, 0.2),
        Vec2(-1.0, 0.15),
        Vec2(-1.0, 0.1),
        Vec2(-1.0, 0.05),
        Vec2(-1.0, 0.0),
        Vec2(-1.0, -0.5),
        Vec2(-1.0, -0.45),
        Vec2(-1.0, -0.4),
        Vec2(-1.0, -0.35),
        Vec2(-1.0, -0.3),
        Vec2(-1.0, -0.25),
        Vec2(-1.0, -0.2),
        Vec2(-1.0, -0.15),
        Vec2(-1.0, -0.1),
        Vec2(-1.0, -0.05),
    )

    private lateinit var circleLayer: BufferedImage
    private var raysLayer: BufferedImage? = null

    // Reusable container for ray segments to avoid per-frame allocations
    private val raysBuffer = mutableListOf<Ray>()

    init {
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        initCircleLayer()
        this.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                val x = (e.x.toDouble() - center.x) / drawScale
                val y = -(e.y.toDouble() - center.y) / drawScale
                initialRayOrigin = Vec2(x, y)
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

        // draw static circle layer first
        g2d.drawImage(circleLayer, 0, 0, null)

        // ensure raysLayer exists and matches size (translucent ARGB)
        if (raysLayer == null || raysLayer!!.width != WIDTH || raysLayer!!.height != HEIGHT) {
            raysLayer = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)
        }

        val buf = raysLayer!!
        val rg = buf.createGraphics()
        try {
            // clear fast (transparent)
            rg.composite = AlphaComposite.Clear
            rg.fillRect(0, 0, WIDTH, HEIGHT)
            rg.composite = AlphaComposite.SrcOver

            // one-time graphics setup for speed
            rg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            rg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED)

            // Build rays for all directions into a single reusable list
            raysBuffer.clear()
            for (dir in rayDirs) {
                val waveType = if (dir.y < 0) 1 else 0
                // create a lightweight initial Ray to pass to tracer (tracer will copy/reset internally)
                val initial = Ray(Vec2(initialRayOrigin.x, initialRayOrigin.y), Vec2(dir.x, dir.y), Vec2(0.0, 0.0), waveType)
                tracer.trace(initial, raysBuffer)
            }

            // Draw all rays in one pass with minimized state changes
            Renderer.drawRays(rg, raysBuffer, center, drawScale, Color.RED, 1.2f)
        } finally {
            rg.dispose()
        }

        // blit the translucent raysLayer to the panel; circleLayer remains visible underneath
        g2d.drawImage(buf, 0, 0, null)
    }

    fun loadPreset(selectedPreset: String) {
        presetReader.fileName = if (selectedPreset == "custom") "simple.txt" else "$selectedPreset.txt"
        preset = presetReader.readContentIntoCircles()
        circles = preset.first
        tracer = RayTracer(circles, ambientVelocity = circles.first().pWaveVelocity, maxDepth = preset.second.toInt() * 2)
        drawScale = STANDARD_RADIUS / preset.second
        initCircleLayer()
        repaint()
    }

    fun updateRayCount(rayCount: Int) {
        // Adjust the number of ray directions based on the slider value
        rayDirs.clear()
        val step = 1.0 / rayCount
        for (i in 0..<rayCount) {
            val y = -0.5 + i * step
            rayDirs.add(Vec2(-1.0, y))
        }
        repaint()
    }

    fun updateVelocities(initialVelocities: List<Float>, finalVelocities: List<Float>) {
        circles = Presets.createSimple(
            vI0 = initialVelocities[0],
            vF0 = finalVelocities[0],
            vI1 = initialVelocities[1],
            vF1 = finalVelocities[1],
            vI2 = initialVelocities[2],
            vF2 = finalVelocities[2],
            vI3 = initialVelocities[3],
            vF3 = finalVelocities[3]
        ).first

        tracer = RayTracer(circles, ambientVelocity = circles.first().pWaveVelocity, maxDepth = preset.second.toInt() * 2)
        repaint()
    }
}
