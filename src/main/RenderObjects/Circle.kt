import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.*

class Circle(val radius: Double, val originX: Double, val originY: Double) {

    fun circleLineIntersection(ray: Ray): Point = circleLineIntersectionImpl(ray.origin, ray.final)
    fun circleLineIntersection(p1:Point, p2:Point): Point = circleLineIntersectionImpl(p1, p2)

    private fun circleLineIntersectionImpl(p1:Point, p2:Point): Point {
        val dx = p2.x - p1.x
        val dy = p2.y - p1.y
        val dr = sqrt(dx * dx + dy * dy)
        val determinant = p1.x*p2.y - p1.y*p2.x
        val discriminant = (radius*dr).pow(2)-determinant.pow(2)

        if (discriminant < 0.0) {
            return Point(200000.0,2000000.0)
        }

        val x1 = (determinant * dy - sgn(dy) * dx * sqrt(discriminant)) / dr.pow(2)
        val x2 = (determinant * dy + sgn(dy) * dx * sqrt(discriminant)) / dr.pow(2)
        val y1 = (-determinant * dx - abs(dy) * sqrt(discriminant)) / dr.pow(2)
        val y2 = (-determinant * dx + abs(dy) * sqrt(discriminant)) / dr.pow(2)

        return if (sqrt((p1.x-x1).pow(2) + (p1.y-y1).pow(2)) < sqrt((p1.x-x2).pow(2) + (p1.y-y2).pow(2))){
            Point(x1, y1)
        } else Point(x2, y2)

    }

    fun normalLineSlope(point: Point):Double {
        return point.y / point.x
    }

    fun render(g2d: Graphics2D, screenWidth: Int, screenHeight: Int, lineColor: Color = Color.BLACK, strokeWidth: Float = 3.0F) {
        g2d.color = lineColor
        g2d.stroke = BasicStroke(strokeWidth)
        val drawX: Int = screenWidth/2 - radius.roundToInt() + originX.roundToInt()
        val drawY: Int = screenHeight/2 - radius.roundToInt() + originY.roundToInt()
        val drawDiameter: Int = (radius*2).roundToInt()
        g2d.drawOval(drawX, drawY, drawDiameter, drawDiameter)

    }

}