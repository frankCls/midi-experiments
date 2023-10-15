import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.extra.midi.MidiDeviceDescription
import org.openrndr.extra.midi.MidiTransceiver
import org.openrndr.extra.noise.*
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.extra.shapes.hobbyCurve
import org.openrndr.math.Vector2
import org.openrndr.math.map
import org.openrndr.math.mod
import org.openrndr.math.normalizationFactor
import org.openrndr.shape.Circle
import org.openrndr.shape.ShapeContour
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan
import kotlin.math.tanh

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */


fun main() = application {

    configure {
        width = 600
        height = 600
    }
    oliveProgram {
        drawer.clear(ColorRGBa.BLACK)
        val circleContour = Circle(drawer.bounds.center, 150.0).contour
        val points = circleContour.bounds.scatter(10.0).take(300)
        extend {
            drawer.fill = ColorRGBa.RED
            drawer.stroke = ColorRGBa.RED
            drawer.strokeWeight = 0.5
            drawer.contour(hobbyCurve(points).sub(0.0, sin(seconds * 0.01) * cos(seconds *0.01)))


        }
    }
}



