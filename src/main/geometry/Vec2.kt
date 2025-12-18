package main.geometry

import kotlin.math.sqrt

@Suppress("NOTHING_TO_INLINE")
data class Vec2(var x: Double, var y: Double) {
    inline fun set(nx: Double, ny: Double) { x = nx; y = ny }
    inline fun copyTo(out: Vec2) { out.x = x; out.y = y }

    operator fun plus(v: Vec2) = Vec2(x + v.x, y + v.y)
    operator fun minus(v: Vec2) = Vec2(x - v.x, y - v.y)
    operator fun times(s: Double) = Vec2(x * s, y * s)
    operator fun div(s: Double) = Vec2(x / s, y / s)

    inline infix fun dot(v: Vec2) = x * v.x + y * v.y

    fun normalizeInPlace(): Double {
        val len = sqrt(x * x + y * y)
        if (len > 0.0) {
            x /= len; y /= len
        }
        return len
    }

    fun normalizedCopy(): Vec2 {
        val len = sqrt(x * x + y * y)
        return if (len > 0.0) Vec2(x / len, y / len) else Vec2(0.0, 0.0)
    }
}
