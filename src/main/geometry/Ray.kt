package main.geometry


data class Ray(var origin: Vec2, var direction: Vec2, var end: Vec2 = origin) {
    init {
        direction = direction.normalized()
        if (end == origin) {
            end = direction * 1000000.0
        }
    }
}