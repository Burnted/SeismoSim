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
        const val WIDTH = 800
        const val HEIGHT = 800
        val center = Vec2(WIDTH / 2.0, HEIGHT / 2.0)
    }

    // TODO: add dialog boxes, sliders, or some kind of mouse clicked listener at startup to allow for placement of rays
    // TODO: add a toolbar with the option to reset rays, increase wave count, close application, and maybe even allow the change to velocity attributes

//    private val mantle      = Circle(Vec2(0.0, 0.0), 306.0, 12.0, 0)
//    private val outerCore   = Circle(Vec2(0.0, 0.0), 174.0, 10.0, 1)
//    private val innerCore   = Circle(Vec2(0.0, 0.0), 63.5, 11.8, 2)
    private val earth = EarthPreset.createEarth()
    private val tracer:RayTracer = RayTracer(earth, ambientVelocity = earth.first().waveVelocity, maxDepth = 10000)

    var initialRayOrigin = Vec2(0.0, 0.0)
    var initialRayDirection = Vec2(1.0, 1.0)

    init {
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        this.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val x = e.x.toDouble() - center.x
                val y = -(e.y.toDouble() - center.y)
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
        val scale = 1.0
        val rays = tracer.trace(Ray(initialRayOrigin, initialRayDirection))

//        Renderer.drawCircle(g2d, outerCore, center, scale)
//        Renderer.drawCircle(g2d, innerCore, center, scale)
//        Renderer.drawCircle(g2d, mantle, center, scale)
        Renderer.drawCircle(g2d, earth.first(), center, scale)
        Renderer.drawRays(g2d, rays, screenCenter = center, scale = scale, Color.RED, strokeWidth = 2f)
    }
}

