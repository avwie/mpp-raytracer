package nl.rjcoding.raytracer

import kotlin.math.sqrt

inline class Tuple(val data: DoubleArray) {
    val x get() = data[0]
    val y get() = data[1]
    val z get() = data[2]
    val w get() = data[3]

    val size get() =  data.size

    val isVector get() = data.size == 4 && RayTracerEnvironment.eq(w, 0.0)
    val isPoint get() = data.size == 4 && RayTracerEnvironment.eq(w, 1.0)

    val magnitude2 get() = data.asIterable().fold(0.0) { acc, x -> acc + x * x}
    val magnitude get() = sqrt(magnitude2)
    val normalized get() = div(magnitude)

    infix fun eq(other: Tuple): Boolean = (data.indices).all { i -> RayTracerEnvironment.eq(data[i], other.data[i]) }

    operator fun plus(other: Tuple): Tuple {
        require(data.size == other.data.size)
        return Tuple((data.indices).map { i -> data[i] + other.data[i] })
    }

    operator fun minus(other: Tuple): Tuple {
        require(data.size == other.data.size)
        return Tuple((data.indices).map { i -> data[i] - other.data[i] })
    }

    operator fun times(other: Tuple): Tuple {
        require(data.size == other.data.size)
        return Tuple((data.indices).map { i -> data[i] * other.data[i] })
    }

    operator fun times(other: Double) = Tuple(data.map { it * other })

    operator fun div(other: Double) = times(1.0 / other)

    fun neg(): Tuple = Tuple(data.map { -it })

    infix fun dot(other: Tuple): Double = (data.indices).fold(0.0) { acc, i -> acc + data[i] * other.data[i]}

    infix fun cross(other: Tuple): Tuple {
        require(data.size == 4)
        require(data.size == other.data.size)
        require(isVector)
        require(other.isVector)

        return vector(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    infix fun hadamard(other: Tuple): Tuple {
        require(data.size == 3)
        require(other.data.size == 3)
        return times(other)
    }

    companion object {
        operator fun invoke(els: List<Double>) = Tuple(DoubleArray(els.size) { i -> els[i] })
        operator fun invoke(vararg x: Double) = Tuple(x.toList())
        operator fun invoke(vararg x: Int) = Tuple(x.map { it.toDouble() }.toList())
    }
}

fun tuple(vararg x: Double) = Tuple(*x)
fun tuple(vararg x: Int) = Tuple(*x)

fun vector(x: Double, y: Double, z: Double) = Tuple(x, y, z, 0.0)
fun vector(x: Int, y: Int, z: Int) = Tuple(x, y, z, 0)

fun point(x: Double, y: Double, z: Double) = Tuple(x, y, z, 1.0)
fun point(x: Int, y: Int, z: Int) = Tuple(x, y, z, 1)

fun color(r: Double, g: Double, b: Double) = Tuple(r, g, b)
fun color(r: Int, g: Int, b: Int) = Tuple(r, g, b)