package main.render

import main.geometry.Circle
import main.geometry.Ray
import main.geometry.Vec2
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Path2D
import kotlin.math.roundToInt

object Renderer {
    private val cachedStroke = mutableMapOf<Float, BasicStroke>()
    private val rayPath = Path2D.Double()

    private fun getStroke(width: Float): BasicStroke {
        return cachedStroke.getOrPut(width) { BasicStroke(width) }
    }

    fun drawCircle(g: Graphics2D, circle: Circle, screenCenter: Vec2, scale: Float, color: Color = Color.BLUE) {
        val cx = (circle.center.x * scale + screenCenter.x).roundToInt()
        val cy = (-circle.center.y * scale + screenCenter.y).roundToInt()
        val r = (circle.radius * scale).roundToInt()

        g.color = color
        g.stroke = getStroke(2f)
        g.drawOval(cx - r, cy - r, r * 2, r * 2)
    }

    fun drawRays(g: Graphics2D, rays: List<Ray>, screenCenter: Vec2, scale: Float, color: Color, strokeWidth: Float = 1.0f) {
        if (rays.isEmpty()) return

        rayPath.reset()

        for (ray in rays) {
            val ox = ray.origin.x * scale + screenCenter.x
            val oy = -ray.origin.y * scale + screenCenter.y
            val ex = ray.end.x * scale + screenCenter.x
            val ey = -ray.end.y * scale + screenCenter.y

            rayPath.moveTo(ox, oy)
            rayPath.lineTo(ex, ey)
        }

        g.stroke = getStroke(strokeWidth)
        g.color = color
        g.draw(rayPath)
    }
}
