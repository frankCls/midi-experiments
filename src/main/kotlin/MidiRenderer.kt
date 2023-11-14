import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Filter
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.extra.compositor.compose
import org.openrndr.extra.compositor.draw
import org.openrndr.extra.compositor.layer
import org.openrndr.extra.compositor.post
import org.openrndr.extra.fx.blur.GaussianBlur
import org.openrndr.extra.fx.blur.LaserBlur
import org.openrndr.extra.fx.dither.ADither
import org.openrndr.extra.fx.grain.FilmGrain
import org.openrndr.extra.midi.*
import org.openrndr.extra.noclear.NoClear
import org.openrndr.extra.noise.Random.vector2
import org.openrndr.extra.noise.cubic
import org.openrndr.extra.noise.perlin
import org.openrndr.extra.noise.simplex
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Polar
import org.openrndr.math.Vector2
import org.openrndr.math.map
import org.openrndr.shape.Rectangle
import org.openrndr.shape.Segment
import visualizers.AmazingMidiVisualizer
import visualizers.OtherMidiVisualizer
import visualizers.PsychedelicEyeMidiVisualizer
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.atan
import kotlin.math.sin
import kotlin.math.tan

data class MidiEventWrapper(val event: MidiEvent, val startTime: Long)

fun main() {

    application {
        configure {
            width = 800
            height = 600
        }

        oliveProgram {
            val colors = listOf(
                ColorRGBa.fromHex("#581845"),
                ColorRGBa.fromHex("#900C3F"),
                ColorRGBa.fromHex("#C70039"),
                ColorRGBa.fromHex("#FF5733"),
                ColorRGBa.fromHex("#FFC300"),
                ColorRGBa.fromHex("#DAF7A6")
            )
            val ttl = 500
            val keyBoardMappings = mapOf(
                66 to TD17.KICK.midiNote, // b
                72 to TD17.HI_HAT_CLOSE_EDGE.midiNote, // h
                83 to TD17.SNARE_HEAD.midiNote, // s
            )

            val renderTarget = renderTarget(width, height) {
                colorBuffer()
//                depthBuffer()
            }
            // Initialize MIDI
            MidiDeviceDescription.list().forEach {
                println(it)
            }
            val midiQueue = ConcurrentLinkedQueue<MidiEventWrapper>()
//            val amazingMidiVisualizer = AmazingMidiVisualizer(drawer)
            val otherMidiVisualizer = OtherMidiVisualizer(drawer)
            try {

                val midi = MidiTransceiver.fromDeviceVendor(this, "TD-17", "Roland")
//                val midi = MidiTransceiver.fromDeviceVendor(program, "Session 2", "Unknown vendor")
//                val midi = MidiTransceiver.fromDeviceVendor(program, "drum", "Apple Inc.")
                midi.noteOn.listen {
                    midiQueue.add(MidiEventWrapper(it, System.currentTimeMillis()))
                }
            } catch (e: Exception) {
                println(e)
            }

            keyboard.keyDown.listen {
                // mimic sending midi events by pressing buttons on the keyboard
                val event = MidiEvent(MidiEventType.NOTE_ON)
                if (keyBoardMappings.containsKey(it.key)) {
                    event.note = keyBoardMappings.getValue(it.key)
                    event.velocity = map(0.0, height.toDouble(), 0.0, 127.0, mouse.position.y).toInt()
                    midiQueue.add(
                        MidiEventWrapper(
                            event, System.currentTimeMillis()
                        )
                    )
                }
            }

//            extend(Screenshots())
//            extend(MidiConsole())
//            extend(NoClear())
            extend {
                val currentTime = System.currentTimeMillis()
                midiQueue.removeIf { midi ->
                    val elapsedTime = currentTime - midi.startTime
                    if (elapsedTime < ttl) {
                        drawer.image(otherMidiVisualizer.draw(midi, seconds, elapsedTime).colorBuffer(0))

                        false
                    } else {
                        true
                    }
                }
            }
        }
    }
}
