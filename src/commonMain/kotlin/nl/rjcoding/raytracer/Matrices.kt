package nl.rjcoding.raytracer

inline class Matrix(val data: DoubleArray) {

    val rows get() = data[0].toInt()
    val cols get() = data[1].toInt()

    operator fun set(row: Int, col: Int, value: Double) { data[row * cols  + col + 2] = value }
    operator fun get(row: Int, col: Int): Double = data[row * cols + col + 2]

    fun row(row: Int): Tuple {
        val indices = (row * cols) until (row + 1) * cols
        return Tuple(data.sliceArray(indices.map { it + 2 }))
    }

    fun col(col: Int): Tuple {
        val indices = col until (rows * cols) step cols
        return Tuple(data.sliceArray(indices.map { it + 2 }))
    }

    fun transpose(): Matrix = Matrix(cols, rows).also { transposed ->
        (0 until rows).forEach { r ->
            (0 until cols).forEach { c ->
                transposed[c, r] = get(r, c)
            }
        }
    }

    operator fun times(other: Matrix): Matrix {
        require(cols == other.rows)
        val result = Matrix(rows, cols)
        (0 until rows).forEach { r ->
            (0 until other.cols).forEach { c ->
                result[r, c] = row(r) dot other.col(c)
            }
        }
        return result
    }

    operator fun times(tuple: Tuple): Tuple {
        val other = Matrix(tuple.size, 1, *tuple.data)
        return times(other).col(0)
    }

    infix fun eq(other: Matrix): Boolean {
        if (data.size != other.data.size) return false
        return (data.indices).all { i -> RayTracerEnvironment.eq(data[i], other.data[i]) }
    }

    companion object {
        operator fun invoke(rows: Int, cols: Int): Matrix {
            val data = DoubleArray(rows * cols + 2)
            data[0] = rows.toDouble()
            data[1] = cols.toDouble()
            return Matrix(data)
        }

        operator fun invoke(rows: Int, cols: Int, vararg values: Double): Matrix = Matrix(rows, cols).also {
            values.forEachIndexed { i, d -> it.data[i + 2] = d }
        }

        operator fun invoke(rows: Int, cols: Int, vararg values: Int): Matrix = Matrix(rows, cols).also {
            values.forEachIndexed { i, d -> it.data[i + 2] = d.toDouble() }
        }

        fun identity(dim: Int) = Matrix(dim, dim).apply {
            (0 until dim).forEach { i ->
                this[i, i] = 1.0
            }
        }
    }
}

fun matrix2(vararg values: Double): Matrix = Matrix(2, 2, *values)
fun matrix2(vararg values: Int): Matrix = Matrix(2, 2, *values)
fun matrix3(vararg values: Double): Matrix = Matrix(3, 3, *values)
fun matrix3(vararg values: Int): Matrix = Matrix(3, 3, *values)
fun matrix4(vararg values: Double): Matrix = Matrix(4, 4, *values)
fun matrix4(vararg values: Int): Matrix = Matrix(4, 4, *values)