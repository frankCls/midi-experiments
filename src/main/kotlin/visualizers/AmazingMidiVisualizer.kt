package visualizers;

import MidiEventWrapper
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector2
import kotlin.math.sin

class AmazingMidiVisualizer(private val drawer: Drawer) :
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
        drawer.isolatedWithTarget(renderTarget) {
            drawer.clear(ColorRGBa.TRANSPARENT)
            drawer.translate(drawer.bounds.center)
            for (i in 0..midi.event.velocity) {
                drawer.fill = null
                drawer.stroke = colors[midi.event.note * midi.event.velocity % colors.size].opacify(alpha)
                drawer.rectangle(
                    Vector2(0.0, 0.0),
                    midi.event.note.toDouble() * 10 * sin(elapsedTime * 0.1),
                    midi.event.note.toDouble() * 10 * sin(seconds * 0.1)
                )
                drawer.rotate(360 / midi.event.velocity.toDouble() * seconds * 0.1)
            }
        }
        return renderTarget
    }
}







