import main.geometry.*
import main.presets.EarthPreset
import main.presets.PresetReader
import main.render.Renderer
import main.sim.RayTracer
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel
import kotlin.math.sign

class SimPanel : JPanel() {
    companion object {
        const val WIDTH = 1200
        const val HEIGHT = 1000
        const val STANDARD_RADIUS = 400f
        val center = Vec2(WIDTH / 2.0, HEIGHT / 2.0)
    }


    // TODO: add dialog boxes, sliders, or some kind of mouse clicked listener at startup to allow for placement of rays
    // TODO: add a toolbar with the option to reset rays, increase wave count, close application, and maybe even allow the change to velocity attributes

    private val preset: Pair<MutableList<Circle>, Float> = PresetReader("earth.txt").readContentIntoCircles()
    private val circles = preset.first
    private val tracer:RayTracer = RayTracer(circles, ambientVelocity = 12.0, maxDepth = 100000)
    private val drawScale = STANDARD_RADIUS / preset.second

    var initialRayOrigin = Vec2(0.0, 0.0)
    var initialRayDirection = Vec2(1.0, 1.0)

    private lateinit var circleLayer: BufferedImage

    init {
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        initCircleLayer()
        this.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                val x = (e.x.toDouble() - center.x) / drawScale
                val y = -(e.y.toDouble() - center.y) / drawScale
                initialRayOrigin = Vec2(x, y)
                initialRayDirection = Vec2(-sign(x), -sign(y))
                repaint()
            }
        })
    }

    private fun initCircleLayer() {
        circleLayer = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)
        val g2d = circleLayer.createGraphics()
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        
        for (circle in circles){
            val color = if (circle.mediumType % 2 == 0){
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

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        g2d.drawImage(circleLayer, 0, 0, null)
        val rays = tracer.trace(Ray(initialRayOrigin, initialRayDirection))

        Renderer.drawRays(g2d, rays, screenCenter = center, scale = drawScale, Color.RED, strokeWidth = 0.7f)
    }
}

