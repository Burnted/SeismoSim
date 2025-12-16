package main.render

import main.geometry.Circle
import main.geometry.Ray
import main.geometry.Vec2
import java.awt.*
import kotlin.math.roundToInt

object Renderer {
    fun drawCircle(g: Graphics2D, circle: Circle, screenCenter: Vec2, scale: Float, color: Color = Color.BLUE) {
        val cx = (circle.center.x * scale + screenCenter.x).roundToInt()
        val cy = (-circle.center.y * scale + screenCenter.y).roundToInt()
        val r = (circle.radius * scale).roundToInt()

        g.color = color
        g.stroke = BasicStroke(2f)
        g.drawOval(cx - r, cy - r, r * 2, r * 2)
    }

    fun drawRays(g: Graphics2D, rays: List<Ray>, screenCenter: Vec2, scale: Float, color: Color, strokeWidth: Float = 2f) {
        for (ray in rays) {
            val ox = (ray.origin.x * scale + screenCenter.x).toInt()
            val oy = (-ray.origin.y * scale + screenCenter.y).toInt()

            val ex = (ray.end.x * scale + screenCenter.x).toInt()
            val ey = (-ray.end.y * scale + screenCenter.y).toInt()

            g.color = if(ray.waveType == 0) Color.MAGENTA else color
            g.stroke = BasicStroke(strokeWidth)
            g.drawLine(ox, oy, ex, ey)
        }

    }
}