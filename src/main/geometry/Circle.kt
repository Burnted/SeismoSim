package main.geometry

class Circle(val center: Vec2, val radius: Double) {
    fun normalAt(p: Vec2): Vec2 = (p - center).normalized()
}