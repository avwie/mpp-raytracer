package nl.rjcoding.raytracer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class CanvasTests {

    abstract fun provideCanvas(width: Int, height: Int): Canvas

    @Test
    fun creating() {
        val canvas = provideCanvas(10, 20)
        assertEquals(10, canvas.width)
        assertEquals(20, canvas.height)

        val pixels = canvas.pixels().toList()
        assertEquals(200, pixels.size)
        assertTrue { pixels.all { it eq color(0, 0, 0)  } }
    }

    @Test
    fun writing() {
        val canvas = provideCanvas(10, 20)
        val red = color(1, 0, 0)
        canvas.setPixel(2, 3, red)
        assertTrue { red eq canvas.getPixel(2, 3) }
    }

    @Test
    fun saving() {
        val canvas = provideCanvas(5, 3)
        val ppm = canvas.saveToString()
        val lines = ppm.lines()
        val header = lines.slice(0 until 3).joinToString("\n")
        assertEquals("""
            P3
            5 3
            255
        """.trimIndent(), header)
    }

    @Test
    fun savingPixelData() {
        val canvas = provideCanvas(5, 3)
        canvas.setPixel(0, 0, color(1.5, 0.0, 0.0))
        canvas.setPixel(2, 1, color(0.0, 0.5, 0.0))
        canvas.setPixel(4, 2, color(-0.5, 0.0, 1.0))
        val ppm = canvas.saveToString()
        val lines = ppm.lines()
        val body = lines.slice(3 until 6).joinToString("\n")
        assertEquals("""
            255 0 0 0 0 0 0 0 0 0 0 0 0 0 0
            0 0 0 0 0 0 0 128 0 0 0 0 0 0 0
            0 0 0 0 0 0 0 0 0 0 0 0 0 0 255
        """.trimIndent(), body)
    }

    @Test
    fun longLines() {
        val canvas = provideCanvas(10, 2)
        val color = color(1.0, 0.8, 0.6)
        canvas.pixelsWithPosition().forEach { (x, y, _) ->
            canvas.setPixel(x, y, color)
        }
        val ppm = canvas.saveToString()
        val lines = ppm.lines()
        val body = lines.slice(3 until 7).joinToString("\n")
        assertEquals("""
            255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204
            153 255 204 153 255 204 153 255 204 153 255 204 153
            255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204
            153 255 204 153 255 204 153 255 204 153 255 204 153
        """.trimIndent(), body)
    }

    @Test
    fun newLine() {
        val canvas = provideCanvas(5, 3)
        val ppm = canvas.saveToString()
        assertEquals('\n', ppm.last())
    }
}

class ArrayCanvasTests : CanvasTests() {
    override fun provideCanvas(width: Int, height: Int): Canvas {
        return ArrayCanvas(width, height)
    }
}