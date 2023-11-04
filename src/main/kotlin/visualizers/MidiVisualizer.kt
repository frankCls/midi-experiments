package visualizers

import MidiEventWrapper
import org.openrndr.draw.Drawer
import org.openrndr.draw.RenderTarget

abstract class MidiVisualizer(drawer: Drawer) {
    abstract fun draw(midi: MidiEventWrapper, seconds: Double, elapsedTime: Long): RenderTarget
}

