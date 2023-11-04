package visualizers;

import MidiEventWrapper
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector2
import org.openrndr.math.map
import kotlin.math.sin

class OtherMidiVisualizer(private val drawer: Drawer) :
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

        val alpha = map(0.0, 1.0, 0.0, 0.7, 1.0 - (elapsedTime.toDouble() / ttl.toDouble()))
        drawer.isolatedWithTarget(renderTarget) {
            drawer.clear(ColorRGBa.TRANSPARENT)
            drawer.translate(drawer.bounds.center)
            for (i in 0..midi.event.velocity) {
                drawer.fill = null
                drawer.strokeWeight = 0.5
                drawer.stroke =
                    colors[midi.event.note * midi.event.velocity % colors.size].opacify(alpha)
                drawer.rectangle(
                    Vector2(
                        sin(seconds * 0.1) * midi.event.note.toDouble(),
                        sin(seconds * 0.1) * midi.event.note.toDouble()
                    ),
                    midi.event.note.toDouble() * 10 * sin(elapsedTime * 0.1),
                    midi.event.note.toDouble() * 10 * sin(elapsedTime * 0.1)
                )
                drawer.rotate(360 / midi.event.velocity.toDouble() * seconds * 0.01)
            }
        }
        return renderTarget
    }
}







