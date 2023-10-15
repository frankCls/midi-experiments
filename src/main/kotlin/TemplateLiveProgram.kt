import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.extra.midi.MidiDeviceDescription
import org.openrndr.extra.midi.MidiTransceiver
import org.openrndr.extra.olive.oliveProgram

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */


fun main() {

    application {

        configure {
            width = 400
            height = 400
        }
        oliveProgram {

            MidiDeviceDescription.list().forEach {
                println(it)
            }
//        val font = loadFont("data/Blanksscript.otf", 50.0)
//        val controller = MidiTransceiver.fromDeviceVendor("TD-17", "Roland")
            val controller = MidiTransceiver.fromDeviceVendor(program,"Bus 1", "Apple Inc.")
//        val controller = MidiTransceiver.fromDeviceVendor("Session 2", "Unknown vendor")
//        val controller = MidiTransceiver.fromDeviceVendor("Bluetooth", "Apple Inc.")

//        val tracker = ADSRTracker()

            controller.noteOn.listen {
                val note = it.note
                println("note on: channel: ${it.channel}, key: $note, velocity: ${it.velocity}")
//            drawer.clear(ColorRGBa.RED)
//            println(drawer)
                drawer.fill = rgb(0.5, 0.5, 0.5)
                drawer.circle(width / 2.0, height / 2.0, 50.0)
            }

            extend {
                drawer.clear(ColorRGBa.PINK)
//            drawer.fontMap = font
                drawer.fill = ColorRGBa.BLACK

//            data class DrumConfig(val text: String, val width: Double, val height: Double)
//
//            fun config(midi: Int): DrumConfig = when (midi) {
//                kick -> DrumConfig("Boom", drawer.width / 2.0, 2.0 * drawer.height / 3)
//                snare_head -> DrumConfig("Tak", drawer.width / 3.0, drawer.height / 2.0)
//                hihat_close_edge -> DrumConfig("ts", drawer.width / 3.0, drawer.height / 3.0)
//                else -> DrumConfig("", 0.0, 0.0)
//            }

//            controller.controlChanged.listen {
//                println("control change: channel: ${it.channel}, control: ${it.control}, value: ${it.value}")
//
//            }

//            controller.noteOff.listen {
//                println("note off:  ${it},")
//            }


            }
        }
    }
}



