import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Shader
import org.openrndr.draw.isolated
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

data class NoteShape(val position: Vector2, val size: Double, val color: ColorRGBa, val startTime: Long)

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
        val midi = MidiTransceiver.fromDeviceVendor(program, "Bus 1", "Apple Inc.")
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

        val shaderCode = """
                #version 330

                uniform float u_Time;
                uniform vec2 u_Resolution;

                out vec4 o_Color;

                void main() {
                    vec2 p = gl_FragCoord.xy / u_Resolution.xy;
                    vec2 uv = p - 0.5;
                    float t = u_Time * 0.05;

                    // Calculate the displacement based on time and UV coordinates
                    float displacement = sin(t + length(uv) * 10.0) * 0.1;

                    // Calculate the final position by adding the displacement
                    vec2 pos = p + normalize(uv) * displacement;

                    // Map the position to the range [-1, 1]
                    vec2 normalizedPos = pos * 2.0 - 1.0;

                    // Calculate the distance from the center
                    float distance = length(normalizedPos);

                    // Apply color based on the distance
                    vec3 color = vec3(1.0 - distance);

                    o_Color = vec4(color, 1.0);
                }
            """.trimIndent()

        midi.noteOn.listen {
            val note = it.note
            println("note on: channel: ${it.channel}, key: $note, velocity: ${it.velocity}")
            val diameter = map(it.velocity.toDouble(), 0.0, 127.0, 0.0, width/100.0)
            val position = Vector2(Random.double(0.0, width.toDouble()), Random.double(0.0, height.toDouble()))
            val startTime = System.currentTimeMillis()
            val color = noteMappings[note] ?: ColorRGBa.WHITE

            val noteCircle = NoteCircle(position, diameter, color, startTime)
            noteQueue.add(noteCircle)
        }

        extend(Screenshots())

        extend {
            drawer.isolated {
                drawer.shadeStyle = shadeStyle {
                    fragmentTransform = "x_fill.rgb = vec3(1.0, 0.0, 0.0);"
                }

//                drawer.clear(ColorRGBa.BLACK)

                while (noteQueue.isNotEmpty()) {
                    val note = noteQueue.poll()
                    val elapsedTime = (System.currentTimeMillis() - note.startTime) / 1000.0
                    val radius = 10.0 + elapsedTime * 50.0

                    drawer.circle(note.position, radius)
                }
            }
        }

        // Function to map a value from one range to another
        fun Double.map(fromStart: Double, fromEnd: Double, toStart: Double, toEnd: Double): Double {
            val fromRange = fromEnd - fromStart
            val toRange = toEnd - toStart
            return (this - fromStart) / fromRange * toRange + toStart
        }
    }
}
