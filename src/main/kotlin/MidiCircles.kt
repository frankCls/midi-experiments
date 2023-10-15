import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.shadeStyle
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.color.presets.DARK_GRAY
import org.openrndr.extra.color.presets.LIGHT_GRAY
import org.openrndr.extra.color.presets.ORANGE
import org.openrndr.extra.color.presets.PURPLE
import org.openrndr.extra.midi.MidiDeviceDescription
import org.openrndr.extra.midi.MidiTransceiver
import org.openrndr.extra.noise.Random
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import org.openrndr.math.map
import java.util.*

data class NoteCircle(val position: Vector2, val diameter: Double, val color: ColorRGBa, val startTime: Long)

fun main() = application {
    configure {
        width = 800
        height = 600
    }

    oliveProgram {
        // Initialize MIDI
        MidiDeviceDescription.list().forEach {
            println(it)
        }
        val midi = MidiTransceiver.fromDeviceVendor(program,"Bus 1", "Apple Inc.")
//            val midi = MidiTransceiver.fromDeviceVendor("TD-17", "Roland")

        // MIDI event listener
        val noteQueue = LinkedList<NoteCircle>() // MIDI note queue (position, diameter, start time)
        val fadeOutDuration = 500L // Fade out duration in milliseconds

        val noteMappings = mapOf(
            36 to ColorRGBa.RED,     // Kick
            37 to ColorRGBa.GREEN,   // Snare Cross Stick
            38 to ColorRGBa.BLUE,    // Snare Head
            40 to ColorRGBa.YELLOW,  // Snare Rim
            48 to ColorRGBa.PINK,    // Tom 1 Head
            50 to ColorRGBa.CYAN,    // Tom 1 Rim
            45 to ColorRGBa.ORANGE,  // Tom 2 Head
            47 to ColorRGBa.PURPLE,  // Tom 2 Rim
            43 to ColorRGBa.WHITE,   // Floor Head
            58 to ColorRGBa.WHITE,   // Floor Rim
            46 to ColorRGBa.GRAY,    // Hi-Hat Open Bow
            26 to ColorRGBa.GRAY,    // Hi-Hat Open Edge
            42 to ColorRGBa.LIGHT_GRAY,  // Hi-Hat Close Bow
            22 to ColorRGBa.LIGHT_GRAY,  // Hi-Hat Close Edge
            44 to ColorRGBa.DARK_GRAY,   // Hi-Hat Pedal
            57 to ColorRGBa(0.2, 0.2, 0.2),   // Cymbal 1 Bow
            52 to ColorRGBa(0.2, 0.2, 0.2),   // Cymbal 1 Edge
            59 to ColorRGBa(0.4, 0.4, 0.4),   // Ride Bow
            51 to ColorRGBa(0.4, 0.4, 0.4),   // Ride Edge
            53 to ColorRGBa(0.4, 0.4, 0.4)    // Ride Bell
        )

        midi.noteOn.listen {
            val note = it.note
            println("note on: channel: ${it.channel}, key: $note, velocity: ${it.velocity}")
            val diameter = map( 0.0, 127.0, 0.0, 150.0, it.velocity.toDouble())
            val position = Vector2(Random.double(0.0, width.toDouble()), Random.double(0.0, height.toDouble()))
            val startTime = System.currentTimeMillis()
            val color = noteMappings[note] ?: ColorRGBa.WHITE

            val noteCircle = NoteCircle(position, diameter, color, startTime)
            noteQueue.add(noteCircle)
        }

        extend(Screenshots())

        extend {
            drawer.clear(ColorRGBa.BLACK)

            val currentTime = System.currentTimeMillis()

            val circlesToRemove = mutableListOf<NoteCircle>()



            for (i in noteQueue.indices.reversed()) {
                val noteCircle = noteQueue[i]
                val elapsedTime = currentTime - noteCircle.startTime

                if (elapsedTime < fadeOutDuration) {
                    val alpha = 1.0 - (elapsedTime.toDouble() / fadeOutDuration.toDouble())
                    drawer.shadeStyle = shadeStyle {
                        fragmentTransform = """
 float pct = distance(p_pos,vec2(0.5));
 vec3 color = vec3(pct, );

 x_fill.rgb *= vec4( color, 1.0);
// float c = cos(sin(p_time) * c_screenPosition.x) * sin(cos(p_time) * c_screenPosition.y * 0.1);
// x_fill.rgb += vec3(p_red, p_green, p_blue);
                    """.trimMargin()
                        parameter("time", elapsedTime.toDouble() * 0.01)
                        parameter("red", noteCircle.color.r)
                        parameter("green", noteCircle.color.g)
                        parameter("blue", noteCircle.color.b)
                        parameter("pos", noteCircle.position)
//                        parameter("note", noteCircle.)
//                        parameter("color", noteCircle.color)
                    }
                    drawer.fill = noteCircle.color.opacify(alpha)
                    drawer.stroke = null
                    val radius = noteCircle.diameter / 2.0

                    drawer.circle(noteCircle.position, radius)
                } else {
                    circlesToRemove.add(noteCircle)
                }
            }

            // Remove completed circles from the queue
            noteQueue.removeAll(circlesToRemove)
        }

        // Function to map a value from one range to another
        fun Double.map(fromStart: Double, fromEnd: Double, toStart: Double, toEnd: Double): Double {
            val fromRange = fromEnd - fromStart
            val toRange = toEnd - toStart
            return (this - fromStart) / fromRange * toRange + toStart
        }
    }
}
