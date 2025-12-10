package main.geometry


data class Ray(var origin: Vec2, var direction: Vec2, var end: Vec2) {
    init {
        direction = direction.normalized()
    }
}