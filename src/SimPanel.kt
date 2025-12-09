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

    var rayX: Double = 0.0
    var rayY: Double = 0.0

    private val circle: Circle = Circle(200.0, 0.0, 0.0)
//    private val ray: Ray = Ray(Point(rayX, rayY), Point(-300.0, 0.0))
//    private var intercept: Point = circle.circleLineIntersection(ray)
//    var normal: Double = circle.normalLineSlope(intercept)

    private val rays: MutableList<Ray> = mutableListOf()
    private val intercepts: MutableList<Point> = mutableListOf()

    init {
        for(i in 0..10){
            val ray = Ray(Point(700.0, 0.0), Point(0.0, 10.0*i))
            rays.add(ray)
            intercepts.add(circle.circleLineIntersection(ray))
        }
        this.preferredSize = Dimension(WIDTH, HEIGHT)
        this.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseMoved(e: MouseEvent) {
//                ray.origin = Point(e.x.toDouble() - WIDTH/2, e.y.toDouble() - HEIGHT/2)
//                intercept = circle.circleLineIntersection(ray)
//                normal = circle.normalLineSlope(intercept)
//                println("intercept: $intercept, normal: $normal")

                for (i in 0..10){
                    rays[i].origin = Point(e.x.toDouble() - WIDTH/2, e.y.toDouble() - HEIGHT/2)
                    intercepts[i] = circle.circleLineIntersection(rays[i])
                }
                repaint()
            }
        })
//        this.addMouseListener(object : MouseAdapter() {
//            override fun mousePressed(e: MouseEvent) {
//                ray.origin = Point(e.x.toDouble() - WIDTH/2, e.y.toDouble() - HEIGHT/2)
//                repaint()
//                println("mouseMoved ${e.x}, ${e.y}")
//            }
//        })
    }

    fun update(){

    }

    override fun paintComponent(g: Graphics?) {
        val g2d = g as Graphics2D
        super.paintComponent(g)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        circle.render(g2d, WIDTH, HEIGHT)
//        ray.render(g2d, WIDTH, HEIGHT)
//        g2d.stroke = BasicStroke(1.0F)
//        g2d.color = Color.BLUE
        //g2d.drawLine(WIDTH/2, HEIGHT/2, intercept.x.roundToInt() + WIDTH/2, (intercept.x*normal).roundToInt() + HEIGHT/2)

        for(i in 0..10){
            val normal = circle.normalLineSlope(intercepts[i])
            rays[i].render(g2d, WIDTH, HEIGHT,intercepts[i].x.roundToInt() + WIDTH/2, (intercepts[i].x*normal).roundToInt() + HEIGHT/2, Color.RED,1.0F)
        }


    }
}

