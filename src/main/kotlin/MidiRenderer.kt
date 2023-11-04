import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.extra.midi.MidiDeviceDescription
import org.openrndr.extra.midi.MidiEvent
import org.openrndr.extra.midi.MidiEventType
import org.openrndr.extra.midi.MidiTransceiver
import org.openrndr.extra.noclear.NoClear
import org.openrndr.extra.noise.Random.vector2
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import org.openrndr.math.map
import org.openrndr.shape.Segment
import visualizers.AmazingMidiVisualizer
import visualizers.OtherMidiVisualizer
import java.util.*
import kotlin.math.sin

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
                depthBuffer()
            }
            // Initialize MIDI
            MidiDeviceDescription.list().forEach {
                println(it)
            }
            val midiQueue = LinkedList<MidiEventWrapper>()
            val amazingMidiVisualizer = AmazingMidiVisualizer(drawer)
            val otherMidiVisualizer = OtherMidiVisualizer(drawer)
            try {

                val midi = MidiTransceiver.fromDeviceVendor(program, "TD-17", "Roland")
//            val midi = MidiTransceiver.fromDeviceVendor(program,"Session 2", "Unknown vendor")
//                val midi = MidiTransceiver.fromDeviceVendor(program, "drum", "Apple Inc.")
                midi.noteOn.listen {
                    val note = it.note
                    println("note on: channel: ${it.channel}, key: $note, velocity: ${it.velocity}")
                    midiQueue.add(MidiEventWrapper(it, System.currentTimeMillis()))
                }
            } catch (e: Exception) {
                println(e)
            }


            val fadeOutDuration = 500L //  Fade out duration in milliseconds

            keyboard.keyDown.listen {
                // mimic sending midi events by pressing buttons on the keyboard
                println(it.key)
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

//        extend(Screenshots())

//            extend(NoClear())
            extend {
                val currentTime = System.currentTimeMillis()

                val eventsToRemove = mutableSetOf<MidiEventWrapper>()

                midiQueue.reversed().forEach { midi ->
                    val elapsedTime = currentTime - midi.startTime
                    if (elapsedTime < fadeOutDuration) {
//                        drawer.image(amazingMidiVisualizer.draw(midi, seconds, elapsedTime).colorBuffer(0))
                        drawer.image(otherMidiVisualizer.draw(midi, seconds, elapsedTime).colorBuffer(0))

                        eventsToRemove.add(midi)
                    }
                }

                // Remove completed events from the queue
                midiQueue.removeAll(eventsToRemove)
//                val averageSnare = midiQueue
//                    .filter { TD17.getDrum(it.event.note) == TD17Drum.SNARE }
//                    .zipWithNext { a, b -> b.startTime - a.startTime }
//                    .average()

//                println(averageSnare)

            }
        }
    }


}
