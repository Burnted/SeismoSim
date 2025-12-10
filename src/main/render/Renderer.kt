package main.render

import main.geometry.Circle
import main.geometry.Ray
import main.geometry.Vec2
import java.awt.*
import kotlin.math.roundToInt

object Renderer {
    fun drawCircle(g: Graphics2D, circle: Circle, screenCenter: Vec2, scale: Double) {
        val cx = (circle.center.x * scale + screenCenter.x).roundToInt()
        val cy = (-circle.center.y * scale + screenCenter.y).roundToInt()
        val r = (circle.radius * scale).roundToInt()

        g.color = Color.BLUE
        g.stroke = BasicStroke(3f)
        g.drawOval(cx - r, cy - r, r * 2, r * 2)
    }

    fun drawRay(g: Graphics2D, ray: Ray, length: Double, screenCenter: Vec2, scale: Double) {
        val ox = (ray.origin.x * scale + screenCenter.x).roundToInt()
        val oy = (-ray.origin.y * scale + screenCenter.y).roundToInt()

        val end = ray.origin + ray.dir * length

        val ex = (end.x * scale + screenCenter.x).roundToInt()
        val ey = (-end.y * scale + screenCenter.y).roundToInt()

        g.color = Color.RED
        g.stroke = BasicStroke(3f)
        g.drawLine(ox, oy, ex, ey)
    }
}