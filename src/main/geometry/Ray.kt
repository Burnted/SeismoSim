package main.geometry

data class Ray(var origin: Vec2, var direction: Vec2, var end: Vec2 = Vec2(0.0, 0.0), val waveType: Int) {
    constructor(ox: Double, oy: Double, dx: Double, dy: Double, wt: Int) : this(
        Vec2(ox, oy),
        Vec2(dx, dy).also { it.normalizeInPlace() },
        Vec2(0.0, 0.0),
        wt
    ) {
        setEndDistance(1_000_000.0)
    }

    init {
        direction.normalizeInPlace()
        if (end.x == 0.0 && end.y == 0.0) {
            setEndDistance(1_000_000.0)
        }
    }

    fun setEndDistance(distance: Double) {
        end.x = origin.x + direction.x * distance
        end.y = origin.y + direction.y * distance
    }

    fun reset(newOrigin: Vec2, newDirection: Vec2, dist: Double = 1_000_000.0) {
        newOrigin.copyTo(origin)
        direction.x = newDirection.x; direction.y = newDirection.y
        direction.normalizeInPlace()
        setEndDistance(dist)
    }
}

