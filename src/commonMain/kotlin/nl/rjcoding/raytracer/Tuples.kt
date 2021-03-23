package nl.rjcoding.raytracer

import kotlin.math.sqrt

typealias Tuple = DoubleArray

fun tuple(els: List<Double>) = DoubleArray(els.size) { i -> els[i] }
fun tuple(vararg x: Double) = DoubleArray(x.size) { i -> x[i] }
fun tuple(vararg x: Int) = DoubleArray(x.size) { i -> x[i].toDouble() }

fun vector(x: Double, y: Double, z: Double): Tuple = tuple(x, y, z, 0.0)
fun vector(x: Int, y: Int, z: Int): Tuple = tuple(x, y, z, 0)

fun point(x: Double, y: Double, z: Double): Tuple = tuple(x, y, z, 1.0)
fun point(x: Int, y: Int, z: Int): Tuple = tuple(x, y, z, 1)

fun color(r: Double, g: Double, b: Double): Tuple = tuple(r, g, b)
fun color(r: Int, g: Int, b: Int): Tuple = tuple(r, g, b)

inline val Tuple.isVector: Boolean get() = this.size == 4 && eq(this[3], 0.0)
inline val Tuple.isPoint: Boolean get() = this.size == 4 && eq(this[3], 1.0)

inline val Tuple.x: Double get() = this[0]
inline val Tuple.y: Double get() = this[1]
inline val Tuple.z: Double get() = this[2]
inline val Tuple.w: Double get() = this[3]

inline val Tuple.magnitude2: Double get() = asIterable().fold(0.0) { acc, x -> acc + x * x }
inline val Tuple.magnitude: Double get() = sqrt(magnitude2)
inline val Tuple.normalized: Tuple get() = div(this, magnitude)

fun eq(a: Tuple, b: Tuple): Boolean = a.size == b.size
        && (a.indices).all { i -> eq(a[i], b[i]) }

fun plus(a: Tuple, b: Tuple): Tuple {
    require(a.size == b.size)
    return tuple((a.indices).map { i -> a[i] + b[i] })
}

fun minus(a: Tuple, b: Tuple): Tuple {
    require(a.size == b.size)
    return tuple((a.indices).map { i -> a[i] - b[i] })
}

fun neg(t: Tuple): Tuple = tuple((t.indices).map { i -> -t[i] })

fun times(t: Tuple, s: Double): Tuple = tuple((t.indices).map { i -> s * t[i] })

fun times(a: Tuple, b: Tuple): Tuple {
    require(a.size == b.size)
    return tuple(a.indices.map { i -> a[i] * b[i] })
}

fun div(t: Tuple, s: Double): Tuple = times(t, 1.0/s)

fun dot(a: Tuple, b: Tuple): Double = (a.indices).fold(0.0) { acc, i -> acc + a[i] * b[i]}

fun cross(a: Tuple, b: Tuple): Tuple {
    require(a.size == 4)
    require(a.size == b.size)
    require(a.isVector)
    require(b.isVector)

    return vector(
            a.y * b.z - a.z * b.y,
            a.z * b.x - a.x * b.z,
            a.x * b.y - a.y * b.x
    )
}

fun hadamardProduct(a: Tuple, b: Tuple): Tuple {
    require(a.size == 3)
    require(b.size == 3)
    return times(a, b)
}