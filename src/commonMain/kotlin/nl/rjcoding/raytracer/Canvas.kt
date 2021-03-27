package nl.rjcoding.raytracer

import kotlin.math.roundToInt

interface Canvas {
    val width: Int
    val height: Int

    fun getPixel(x: Int, y: Int): Tuple
    fun setPixel(x: Int, y: Int, color: Tuple)

    operator fun get(x: Int, y: Int): Tuple = getPixel(x, y)
}

fun Canvas.pixelsWithPosition(): Sequence<Triple<Int, Int, Tuple>> = sequence {
    (0 until height).forEach { y ->
        (0 until width).forEach { x ->
            yield(Triple(x, y, getPixel(x, y)))
        }
    }
}

fun Canvas.pixels(): Sequence<Tuple> = pixelsWithPosition().map { it.third }

fun Canvas.save(appendable: Appendable) = appendable.run {
    appendLine("P3")
    appendLine("$width $height")
    appendLine("255")

    val currentLine = StringBuilder()
    var line = 0
    pixelsWithPosition().forEach { (_, y, pixel) ->
        if (y != line) {
            appendLine(currentLine.trimEnd())
            currentLine.clear()
        }
        line = y

        pixel.data.forEach { f ->
            val i = (f * 255).roundToInt().coerceIn(0, 255).toString()
            if (currentLine.length + 1 + i.length > 70) {
                appendLine(currentLine.trimEnd())
                currentLine.clear()
            }

            currentLine.append(i)
            currentLine.append(" ")
        }
    }
    appendLine(currentLine.trimEnd())
    appendLine()
}

fun Canvas.saveToString(): String = StringBuilder().also { save(it) }.toString()

class ArrayCanvas(override val width: Int, override val height: Int) : Canvas {

    private val data = DoubleArray(width * height * 3)

    override fun getPixel(x: Int, y: Int): Tuple {
        val startIndex = posToIndex(x, y)
        return Tuple(data.sliceArray(startIndex until startIndex + 3))
    }

    override fun setPixel(x: Int, y: Int, color: Tuple) {
        val startIndex = posToIndex(x, y)
        (0 until 3).forEach { i ->
            data[startIndex + i] = color.data[i]
        }
    }

    private fun posToIndex(x: Int, y: Int): Int {
        return (y * width + x) * 3
    }
}

