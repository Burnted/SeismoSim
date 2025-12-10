package main.geometry

import kotlin.math.hypot

data class Vec2(val x: Double, val y: Double) {
    operator fun plus(v: Vec2) = Vec2(x + v.x, y + v.y)
    operator fun minus(v: Vec2) = Vec2(x - v.x, y - v.y)
    operator fun times(s: Double) = Vec2(x * s, y * s)
    operator fun div(v: Vec2) = Vec2(x / v.x, y / v.y)
    operator fun unaryMinus() = Vec2(-x, -y)

    infix fun dot(v: Vec2) = x * v.x + y * v.y
    fun length() = hypot(x, y)
    fun normalized() = this * (1.0 / length())

    fun perpendicular() = Vec2(-y, x)

}