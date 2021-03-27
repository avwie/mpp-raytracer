package nl.rjcoding.raytracer

import kotlin.math.abs

object RayTracerEnvironment {

    private val EPSILON = 1E-9

    var epsilon: Double = EPSILON

    fun reset() {
        epsilon = EPSILON
    }

    fun eq(a: Double, b: Double) = abs(a - b) < epsilon 

    fun with(
        epsilon: Double = this.epsilon,
        block: () -> Unit
    ) {
        this.epsilon = epsilon
        block()
        reset()
    }
}