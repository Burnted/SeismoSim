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
        const val HEIGHT = 1200
        val center = Vec2(WIDTH / 2.0, HEIGHT / 2.0)
    }

    // TODO: add dialog boxes, sliders, or some kind of mouse clicked listener at startup to allow for placement of rays
    // TODO: add a toolbar with the option to reset rays, increase wave count, close application, and maybe even allow the change to velocity attributes

    private val mantle = Circle(Vec2(0.0, 0.0), 450.0, 3000.0)
    private val outerCore = Circle(Vec2(0.0, 0.0), 225.0, 1000.0)
    private val innerCore = Circle(Vec2(0.0, 0.0), 100.0, 1200.0)
    var listOfCircles = mutableListOf<Circle>()
    private val tracer:RayTracer

    var initialRayOrigin = Vec2(0.0, 0.0)
    var initialRayDirection = Vec2(1.0, 1.0)

    init {
        for (i in 1..1000){
            listOfCircles.add(Circle(Vec2(0.0, 0.0), 0.5*i, 500.0+i*5.0))
        }
        tracer = RayTracer(listOfCircles, ambientVelocity = 50.0, maxDepth = 2500)
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        this.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                val x = e.x.toDouble() - center.x
                val y = -(e.y.toDouble() - center.y)
                initialRayOrigin = Vec2(x, y)
                initialRayDirection = Vec2(-sign(x), -sign(y))
                println("e.x: ${e.x}, e.y: ${e.y}")
                repaint()
            }
        })
    }


    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val scale = 1.0
        val rays = tracer.trace(Ray(initialRayOrigin, initialRayDirection))


        for (circle in listOfCircles) {
            Renderer.drawCircle(g2d, circle, center, scale)

        }
        //Renderer.drawCircle(g2d, outerCore, center, scale)
        //Renderer.drawCircle(g2d, innerCore, center, scale)
        Renderer.drawRays(g2d, rays, screenCenter = center, scale = scale, Color.RED, strokeWidth = 2f)
    }
}

