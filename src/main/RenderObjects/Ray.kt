import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.*

class Ray(var origin: Point, var final: Point = Point(0.0, 0.0)) {

    val rayLength = sqrt((origin.x - final.x).pow(2) + (origin.y - final.y).pow(2))

    fun render(g2d: Graphics2D, screenWidth: Int, screenHeight: Int, drawX2: Int = final.x.roundToInt() + screenHeight / 2, drawY2: Int = final.y.roundToInt() + screenWidth / 2, lineColor: Color = Color.BLACK, strokeWidth: Float = 3.0F) {
        g2d.color = lineColor
        g2d.stroke = BasicStroke(strokeWidth)
        val drawX1: Int = origin.x.roundToInt() + screenWidth / 2
        val drawY1: Int = origin.y.roundToInt() + screenHeight / 2

        g2d.drawLine(drawX1, drawY1, drawX2, drawY2)
    }
}