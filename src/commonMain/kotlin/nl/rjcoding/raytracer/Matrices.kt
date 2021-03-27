package nl.rjcoding.raytracer

data class Matrix(val rows: Int, val cols: Int) {
    private val data = DoubleArray(rows * cols)
    
    operator fun set(row: Int, col: Int, value: Double) { data[row * cols  + col] = value }
    operator fun get(row: Int, col: Int): Double = data[row * cols + col]

    fun row(row: Int): Tuple = data.sliceArray(row * cols until (row + 1) * cols)
    fun col(col: Int): Tuple {
        val i = (col until (rows * cols) step cols).toList()
        return data.sliceArray(i)
    }

    operator fun times(other: Matrix): Matrix {
        require(cols == other.rows)
        val result = Matrix(rows, cols)
        (0 until rows).forEach { r ->
            (0 until other.cols).forEach { c ->
                result[r, c] = dot(row(r), other.col(c))
            }
        }
        return result
    }

    operator fun times(tuple: Tuple): Tuple {
        val other = Matrix(tuple.size, 1, *tuple)
        return times(other).col(0)
    }

    companion object {
        operator fun invoke(rows: Int, cols: Int, vararg values: Double): Matrix = Matrix(rows, cols).apply {
            values.forEachIndexed { i, d ->
                this[i / 4, i % 4] = d
            }
        }

        operator fun invoke(rows: Int, cols: Int, vararg values: Int): Matrix = Matrix(rows, cols).apply {
            values.forEachIndexed { i, d ->
                this[i / 4, i % 4] = d.toDouble()
            }
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