//import org.openrndr.application
//import org.openrndr.color.ColorRGBa
//import org.openrndr.draw.loadFont
//import org.openrndr.draw.loadImage
//import org.openrndr.draw.tint
//import org.openrndr.extra.midi.MidiTransceiver
//import kotlin.math.cos
//import kotlin.math.sin
//
//fun main() = application {
//    configure {
//        width = 768
//        height = 576
//    }
//
//    program {
//        val font = loadFont("data/Blanksscript.otf", 50.0)
//        val controller = MidiTransceiver.fromDeviceVendor("TD-17", "Roland")
//        extend {
//            drawer.clear(ColorRGBa.PINK)
//            drawer.fontMap = font
//            drawer.fill = ColorRGBa.BLACK
//
//            data class DrumConfig(val text: String, val width: Double, val height: Double)
//
//            fun config(midi: Int): DrumConfig = when (midi) {
//                kick -> DrumConfig("Boom", drawer.width / 2.0, 2.0 * drawer.height / 3)
//                snare_head -> DrumConfig("Tak", drawer.width / 3.0, drawer.height / 2.0)
//                hihat_close_edge -> DrumConfig("ts", drawer.width / 3.0, drawer.height / 3.0)
//                else -> DrumConfig("", 0.0, 0.0)
//            }
//
//            controller.controlChanged.listen {
//                println("control change: channel: ${it.channel}, control: ${it.control}, value: ${it.value}")
//
//            }
//            controller.noteOn.listen {
//                val note = it.note
//                println("note on: channel: ${it.channel}, key: $note, velocity: ${it.velocity}")
//
////                val (text, width, height) = config(note)
//                drawer.text("boom", drawer.width / 2.0, drawer.height / 2.0)
////                writer {
////                    newLine()
////                    text("boomn")
////                }
//            }
////            controller.noteOff.listen {
////                println("note off:  ${it},")
////            }
//
//
//        }
//
//    }
//}
