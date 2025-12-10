import main.geometry.*
import main.render.Renderer
import main.sim.RayTracer
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel

class SimPanel : JPanel() {
    companion object {
        const val WIDTH = 1200
        const val HEIGHT = 1200
    }

    private val circle = Circle(Vec2(0.0, 0.0), 300.0)
    private val tracer = RayTracer(listOf(circle), v1 = 1600.0, v2 = 1500.0, maxDepth = 10)

    init {
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        this.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                repaint()
            }
        })
    }


    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        val center = Vec2(width / 2.0, height / 2.0)
        val scale = 1.0
        val rays = tracer.trace(Ray(Vec2(100.0, 500.0), Vec2(-1.0, -10.1), Vec2(50.0, 200.0) + Vec2(-1.0, -10.1) * 1000.0))

        Renderer.drawCircle(g2d, circle, center, scale)
        Renderer.drawRays(g2d, rays, screenCenter = center, scale = scale, Color.RED)
    }
}

