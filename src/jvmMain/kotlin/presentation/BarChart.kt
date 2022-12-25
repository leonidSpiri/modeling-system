package presentation

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class BarChart : JPanel() {
    private val bars: MutableMap<Color, List<Int>> = LinkedHashMap()
    fun addBar(color: Color, value: List<Int>) {
        bars[color] = value
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        var max = Int.MIN_VALUE
        for (value in bars.values) {
            max = max.coerceAtLeast(value[0])
        }

        val width = width / bars.size - 2
        var x = 1
        for (color in bars.keys) {
            val value = bars[color]!!
            val height = ((height - 5) * (value[0].toDouble() / max)).toInt()
            g.color = color
            g.fillRect(x, getHeight() - height - 25, width, height)
            g.color = Color.black
            g.drawRect(x, getHeight() - height - 25, width, height)
            val str = if (value[1] == 1) "${value[1]} объект - ${value[0]} раз"
            else if (value[1] in 2..4) "${value[1]} объекта - ${value[0]} раз"
            else "${value[1]} объектов - ${value[0]} раз"
            g.drawString(str, x + 10, getHeight() - 10)
            x += width + 5
        }
    }

    override fun getPreferredSize() = Dimension((bars.size * 10 + 5) * 15, 500)

    companion object {
        val colors = listOf(
            Color.red,
            Color.green,
            Color.blue,
            Color.yellow,
            Color.orange,
            Color.pink,
            Color.cyan,
            Color.magenta
        )
    }
}