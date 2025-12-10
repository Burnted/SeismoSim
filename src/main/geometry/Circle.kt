package main.geometry

class Circle(val center: Vec2, val radius: Double, val waveVelocity: Double) {
    fun normalAt(p: Vec2): Vec2 = (p - center).normalized()
    //TODO: add velocity attributes to circle class
}