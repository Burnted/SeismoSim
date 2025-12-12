import main.geometry.*
import main.render.Renderer
import main.sim.RayTracer
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import kotlin.math.sign

class SimPanel : JPanel() {
    companion object {
        const val WIDTH = 1200
        const val HEIGHT = 1000
        val center = Vec2(WIDTH / 2.0, HEIGHT / 2.0)
    }

    // TODO: add dialog boxes, sliders, or some kind of mouse clicked listener at startup to allow for placement of rays
    // TODO: add a toolbar with the option to reset rays, increase wave count, close application, and maybe even allow the change to velocity attributes

    private val mantle      = Circle(Vec2(0.0, 0.0), 306.0f, 12.0, 0)
    private val midMantle   = Circle(Vec2(0.0, 0.0), 250.0f, 15.0, 0)
    private val outerCore   = Circle(Vec2(0.0, 0.0), 174.0f, 9.0, 1)
    private val innerCore   = Circle(Vec2(0.0, 0.0), 63.5f, 11.8, 2)
    private val simple = mutableListOf(mantle, midMantle, outerCore, innerCore)
    private val earth = EarthPreset.createEarth()
    private val tracer:RayTracer = RayTracer(simple, ambientVelocity = 12.0, maxDepth = 100000)

    var initialRayOrigin = Vec2(0.0, 0.0)
    var initialRayDirection = Vec2(1.0, 1.0)

    init {
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        this.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                val x = (e.x.toDouble() - center.x)* 1
                val y = -(e.y.toDouble() - center.y)* 1
                initialRayOrigin = Vec2(x, y)
                initialRayDirection = Vec2(-sign(x), -sign(y))
                repaint()
            }
        })
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED)
        val scale = 1.0//0.0625
        val result = tracer.trace(Ray(initialRayOrigin, initialRayDirection))
        val rays = result.first
        val reflections = result.second

        Renderer.drawCircle(g2d, outerCore, center, scale)
        Renderer.drawCircle(g2d, innerCore, center, scale)
        Renderer.drawCircle(g2d, mantle, center, scale)
        Renderer.drawCircle(g2d, midMantle, center, scale)
       // Renderer.drawCircle(g2d, earth.first(), center, scale)
        Renderer.drawRays(g2d, reflections, screenCenter = center, scale = scale, Color.CYAN, strokeWidth = 2f)
        Renderer.drawRays(g2d, rays, screenCenter = center, scale = scale, Color.RED, strokeWidth = 2f)
    }
}

