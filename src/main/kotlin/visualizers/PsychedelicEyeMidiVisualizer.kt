package visualizers;

import MidiEventWrapper
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import kotlin.math.atan
import kotlin.math.sin
import kotlin.math.tan

class PsychedelicEyeMidiVisualizer(private val drawer: Drawer) :
    MidiVisualizer(drawer) {

    private val ttl = 500
    private val colors = listOf(
        ColorRGBa.fromHex("#581845"),
        ColorRGBa.fromHex("#900C3F"),
        ColorRGBa.fromHex("#C70039"),
        ColorRGBa.fromHex("#FF5733"),
        ColorRGBa.fromHex("#FFC300"),
        ColorRGBa.fromHex("#DAF7A6")
    )

    private val renderTarget = renderTarget(800, 600) {
        colorBuffer()
    }

    override fun draw(midi: MidiEventWrapper, seconds: Double, elapsedTime: Long): RenderTarget {

        val alpha = 1.0 - (elapsedTime.toDouble() / ttl.toDouble())
        drawer.rectangles {
            repeat(midi.event.velocity) {
                fill = null
                strokeWeight = 0.5
                stroke =
                    colors[midi.event.note * midi.event.velocity % colors.size].opacify(alpha)
                rectangle(
                    Rectangle(
                        Vector2(
                            tan(elapsedTime * 0.01) * midi.event.note.toDouble(),
                            tan(elapsedTime * 0.01) * midi.event.note.toDouble()
                        ),
                        midi.event.note.toDouble() * 10 * atan(elapsedTime * 0.1),
                        midi.event.note.toDouble() * 10 * atan(elapsedTime * 0.1)
                    ),
                    it * 360 / midi.event.velocity.toDouble() * seconds * 0.01
                )

            }
        }
        return renderTarget
    }
}







