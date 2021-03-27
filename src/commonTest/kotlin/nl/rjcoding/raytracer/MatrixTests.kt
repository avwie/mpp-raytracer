package nl.rjcoding.raytracer

import kotlin.test.*

class MatrixTests {

    @Test
    fun create4() {
        val m = matrix4(1.0, 2.0, 3.0, 4.0, 5.5, 6.5, 7.5, 8.5, 9.0, 10.0, 11.0, 12.0, 13.5, 14.5, 15.5, 16.5)
        assertEquals(1.0, m[0, 0])
        assertEquals(4.0, m[0, 3])
        assertEquals(5.5, m[1, 0])
        assertEquals(7.5, m[1, 2])
        assertEquals(11.0, m[2, 2])
        assertEquals(13.5, m[3, 0])
        assertEquals(15.5, m[3, 2])

        assertTrue { tuple(5.5, 6.5, 7.5, 8.5) eq m.row(1) }
        assertTrue { tuple(2.0, 6.5, 10.0, 14.5) eq m.col(1) }
    }

    @Test
    fun create2() {
        val m = matrix2(-3.0, 5.0, 1.0, -2.0)
        assertEquals(-3.0, m[0, 0])
        assertEquals(5.0, m[0, 1])
        assertEquals(1.0, m[1, 0])
        assertEquals(-2.0, m[1, 1])
    }

    @Test
    fun equality() {
        val m1 = matrix4(1, 2, 3, 4, 5, 6, 7, 8, 9, 5, 4, 3, 2)
        val m2 = matrix4(1, 2, 3, 4, 5, 6, 7, 8, 9, 5, 4, 3, 2)
        assertTrue { m1 eq m2 }

        val m3 = matrix4(2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4)
        assertFalse { m1 eq m3 }
        assertFalse { m2 eq m3 }
    }

    @Test
    fun matrixMultiplication() {
        val m1 = matrix4(1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2)
        val m2 = matrix4(-2, 1, 2, 3, 3, 2, 1, -1, 4, 3, 6, 5, 1, 2, 7, 8)
        val m3 = m1 * m2

        val expected = matrix4(20, 22, 50, 48, 44, 54, 114, 108, 40, 58, 110, 102, 16, 26, 46, 42)
        assertTrue { expected eq m3 }
    }

    @Test
    fun tupleMultiplication() {
        val m1 = matrix4(1, 2, 3, 4, 2, 4, 4, 2, 8, 6, 4, 1, 0, 0, 0, 1)
        val t = tuple(1, 2, 3, 1)
        val result = m1 * t
        val expected = tuple(18, 24, 33, 1)
        assertTrue { expected eq result }
    }

    @Test
    fun identity() {
        val m1 = matrix4(1, 2, 3, 4, 2, 4, 4, 2, 8, 6, 4, 1, 0, 0, 0, 1)
        val t = tuple(1, 2, 3, 1)
        val i4 = Matrix.identity(4)
        assertTrue { m1 eq m1 * i4 }
        assertTrue { t eq i4 * t }
    }
}