package main.geometry

data class Ray(var origin: Vec2, var direction: Vec2, var end: Vec2 = Vec2(0.0, 0.0), val waveType: Int) {
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

    /**
     * Reuse this Ray: copy origin values, set and normalize direction, and set end distance.
     * Keeps allocations low by reusing contained Vec2s.
     */
    fun reset(newOrigin: Vec2, newDirection: Vec2, dist: Double = 1_000_000.0) {
        newOrigin.copyTo(origin)
        direction.x = newDirection.x; direction.y = newDirection.y
        direction.normalizeInPlace()
        setEndDistance(dist)
    }

    // helper to copy this ray into a new Ray (used when recording segments)
    fun copyForRecord(): Ray {
        return Ray(Vec2(origin.x, origin.y), Vec2(direction.x, direction.y), Vec2(end.x, end.y), waveType)
    }
}
