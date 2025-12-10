import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JPanel
import kotlin.math.roundToInt

class SimPanel : JPanel() {
    companion object {
        const val WIDTH = 800
        const val HEIGHT = 800
    }

    private val circle: Circle = Circle(200.0, 0.0, 0.0)
    private val ray: Ray = Ray(Point(10000.0, 10000.0), Point(0.0, 160.0))
    private var intercept: Point = circle.circleLineIntersection(ray)
    var normal: Double = circle.normalLineSlope(intercept)

    private var refractedRay: Ray = Ray(Point(199.0, 199.0))

    init {
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        this.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                ray.origin = Point(e.x.toDouble() - WIDTH/2, e.y.toDouble() - HEIGHT/2)
                intercept = circle.circleLineIntersection(ray)
                normal = circle.normalLineSlope(intercept)
                refractedRay.origin = intercept
                val slope1: Double = calcSlopeFromPoints(ray.origin, ray.final)
                val incident: Double = calcIncidentAngle(slope1, normal)
                val transient: Double = calcTransAngle(incident)
                val slope2: Double = calcSlopeFromAngle(transient, normal)
                refractedRay.final = Point(0.0, intercept.y - intercept.x*slope2)
                repaint()
            }
        })
    }


    override fun paintComponent(g: Graphics?) {
        val g2d = g as Graphics2D
        super.paintComponent(g)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        circle.render(g2d, WIDTH, HEIGHT)
        ray.render(g2d, WIDTH, HEIGHT,intercept.x.roundToInt() + WIDTH/2, (intercept.x*normal).roundToInt() + HEIGHT/2, Color.RED,1.0F)
        refractedRay.render(g2d, WIDTH, HEIGHT, lineColor = Color.BLUE, strokeWidth = 1.0F)
    }
}

