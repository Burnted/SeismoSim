// kotlin
package main.app

import main.geometry.*
import main.presets.PresetReader
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

    private val preset: Pair<MutableList<Circle>, Float> = PresetReader("earth.txt").readContentIntoCircles()
    private val circles = preset.first
    private val tracer: RayTracer = RayTracer(circles, ambientVelocity = 12.0f, maxDepth = 100000)
    private val drawScale = STANDARD_RADIUS / preset.second

    var initialRayOrigin = Vec2(400.0, 400.0)

    var rayDirs = mutableListOf(
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

    // Use a translucent BufferedImage for rays so underlying circleLayer shows through
    private var raysLayer: BufferedImage? = null

    init {
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        initCircleLayer()
        this.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        for (circle in circles) {
            val color = if (circle.mediumType % 2 == 0) {
                Color(200, 200, 255)
            } else {
                Color(150, 150, 255)
            }
            Renderer.drawCircle(g2d, circle, center, drawScale, color)
        }
        g2d.dispose()
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

        // draw all rays into the offscreen translucent image
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

            // draw each ray segment directly with integer coords
            for (dir in rayDirs) {
                val waveType = if (dir.y < 0){
                    1
                } else {
                    0
                }
                val rays = tracer.trace(Ray(initialRayOrigin, dir, waveType = waveType))
                Renderer.drawRays(rg, rays, center, drawScale, Color.RED, 1.1f)
            }
        } finally {
            rg.dispose()
        }

        // blit the translucent raysLayer to the panel; circleLayer remains visible underneath
        g2d.drawImage(buf, 0, 0, null)
    }
}
