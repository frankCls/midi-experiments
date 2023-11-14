package visualizers;

import MidiEventWrapper
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.blur.GaussianBlur
import org.openrndr.extra.fx.blur.LaserBlur
import org.openrndr.math.Vector2
import org.openrndr.math.map
import kotlin.math.sin
import kotlin.math.tan

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
        val ttl = map(0.0, 127.0, 500.0, 2000.0, midi.event.velocity.toDouble())
        val alpha = map(0.0, 1.0, 0.0, 0.6, 1.0 - (elapsedTime.toDouble() / ttl.toDouble()))


        drawer.isolatedWithTarget(renderTarget) {
            drawer.translate(drawer.bounds.center)
            drawer.clear(ColorRGBa.TRANSPARENT)
            repeat(midi.event.velocity) {
                fill = null
                strokeWeight = 0.5
                drawer.stroke =
                    colors[midi.event.note % colors.size].opacify(alpha)
                drawer.rectangle(
                    sin(seconds * 0.05) * midi.event.note.toDouble(),
                    sin(seconds * 0.05) * midi.event.note.toDouble(),
                    midi.event.note.toDouble() * 10 * tan(elapsedTime * 0.01),
                    midi.event.note.toDouble() * 10 * tan(elapsedTime * 0.01)
                )
                drawer.rotate(seconds * 0.1 * (360 / midi.event.velocity.toDouble()))
            }
        }
        return renderTarget
    }
}







