import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noclear.NoClear
import org.openrndr.extra.noise.Random
import org.openrndr.extra.noise.perlinLinear
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import org.openrndr.shape.Segment
import kotlin.math.cos
import kotlin.math.sin

fun main() = application {
    configure {
        width = 600
        height = 600
    }



    oliveProgram {

        data class Circle(val position: Vector2, val radius: Double, val color:ColorRGBa)
        extend(NoClear())
        extend {
            val points = List(60) {
                val x = cos(it * cos(seconds / 10))* 210.0
                val y = sin(it * sin(seconds / 10))* 210.0
                val position = Vector2(x, y)
                val radius = sin(seconds)
                val red = perlinLinear(x.toInt(), x * 0.01, y * 0.01)
                val green = perlinLinear(y.toInt(),x * 0.01, y * 0.01)
                val blue = Random.perlin(x * 0.01, y * 0.01)
//                val color = ColorRGBa(red, green, red).opacify(cos(seconds *0.5))
                val color = ColorHSVa(red, green, red).opacify(cos(seconds *0.5)).toRGBa()
                Circle(position, radius, color)
            }
            drawer.translate(drawer.bounds.center)
//            drawer.clear(ColorRGBa.BLACK)
//            val color = ColorRGBa(cos(seconds), Random.perlin(seconds * 0.01), cos(seconds)).opacify(sin(seconds / 10))
//            drawer.fill = color
//            drawer.stroke = null
//            drawer.strokeWeight = 2.0
            drawer.circles {
//                points.forEach {
//                    fill = it.color
//                    circle(it.position, it.radius)
//                }
            }

            drawer.segments(points.zipWithNext { a, b ->
                drawer.stroke = a.color.opacify(0.9)
                drawer.strokeWeight = 0.5
                Segment(a.position, b.position)
            })

        }
    }
}