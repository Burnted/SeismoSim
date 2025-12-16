package main.geometry

class Circle(val center: Vec2 = Vec2(0.0,0.0), val radius: Float, val waveVelocity: Float, val mediumType:Int) {
    fun normalAt(p: Vec2): Vec2 = (p - center).normalizedCopy()
}