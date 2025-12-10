import kotlin.math.*

fun sgn(num: Double): Double {
    if (num < 0.0) return -1.0
    return 1.0
}

fun calcSlopeFromPoints(origin: Point, final: Point): Double {
    return (final.y - origin.y) / (final.x - origin.x)
}

fun calcSlopeFromAngle(angle: Double, slope: Double): Double {
    return -(tan(angle) - slope) / (slope * tan(angle) + 1)
}

fun calcIncidentAngle(slope1: Double, slope2: Double): Double {
    return abs(atan((slope1-slope2) / (1 + slope1 * slope2)))
}

fun calcTransAngle(angle: Double): Double {
    return asin((500.0* sin(angle)) / 1000.0)
}