import main.geometry.*
import main.render.Renderer
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel

class SimPanel : JPanel() {
    companion object {
        const val WIDTH = 800
        const val HEIGHT = 800
    }

    private val circle = Circle(Vec2(0.0, 0.0), 100.0)
    private val ray = Ray(Vec2(-200.0, 50.0), Vec2(1.0, -0.2))


    init {
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        this.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                repaint()
            }
        })
    }


    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        val center = Vec2(width / 2.0, height / 2.0)
        val scale = 1.0

        Renderer.drawCircle(g2d, circle, center, scale)
        Renderer.drawRay(g2d, ray, length = 1000.0, screenCenter = center, scale = scale)
    }
}

