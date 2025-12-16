package main.geometry

import kotlin.math.sqrt

data class Vec2(var x: Double, var y: Double) {
    fun set(nx: Double, ny: Double) { x = nx; y = ny }
    fun copyTo(out: Vec2) { out.x = x; out.y = y }

    operator fun plus(v: Vec2) = Vec2(x + v.x, y + v.y)
    operator fun minus(v: Vec2) = Vec2(x - v.x, y - v.y)
    operator fun times(s: Double) = Vec2(x * s, y * s)
    operator fun div(s: Double) = Vec2(x / s, y / s)

    // In-place ops to avoid allocations
    fun addInPlace(v: Vec2) { x += v.x; y += v.y }
    fun subInPlace(v: Vec2) { x -= v.x; y -= v.y }
    fun mulInPlace(s: Double) { x *= s; y *= s }
    fun setMul(v: Vec2, s: Double) { x = v.x * s; y = v.y * s }
    fun setAdd(a: Vec2, b: Vec2) { x = a.x + b.x; y = a.y + b.y }

    infix fun dot(v: Vec2) = x * v.x + y * v.y
    fun length() = sqrt(x * x + y * y)
    fun length2() = x * x + y * y

    fun normalizeInPlace(): Double {
        val len = length()
        if (len != 0.0) {
            x /= len; y /= len
        }
        return len
    }

    fun normalizedCopy(): Vec2 {
        val len = length()
        return if (len != 0.0) Vec2(x / len, y / len) else Vec2(0.0, 0.0)
    }

    fun perpendicular() = Vec2(-y, x)
}
