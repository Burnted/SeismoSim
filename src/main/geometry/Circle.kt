package main.geometry

class Circle(val center: Vec2 = Vec2(0.0, 0.0), val radius: Float, val pWaveVelocity: Float, val sWaveVelocity: Float = 0f, val mediumType: Int) {

    inline fun normalAt(p: Vec2, out: Vec2) {
        out.x = p.x - center.x
        out.y = p.y - center.y
        out.normalizeInPlace()
    }
}
