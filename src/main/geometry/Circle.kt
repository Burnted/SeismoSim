package main.geometry

class Circle(val center: Vec2 = Vec2(0.0,0.0), val radius: Double, val waveVelocity: Double, val layerDepth: Int) {
    fun normalAt(p: Vec2): Vec2 = (p - center).normalized()
}