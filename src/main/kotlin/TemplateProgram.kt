import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.renderTarget
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.ffmpeg.VideoPlayerFFMPEG

fun main() = application {
    configure {
        width = 1280
        height = 720
    }

    oliveProgram {
        val renderTarget = renderTarget(width, height) {
            colorBuffer()
        }
        val videoPlayer = VideoPlayerFFMPEG.fromDevice()
        videoPlayer.play()



        val sphere = shadeStyle {
            fragmentTransform = """
                vec2 uv = c_boundsPosition.xy;
                vec4 videoColor = texture(p_video, vec2(uv.x, 1.0 - uv.y));
                x_fill = videoColor;
            """
            parameter("video", renderTarget.colorBuffer(0))
        }

        extend {
            drawer.withTarget(renderTarget) {
                videoPlayer.draw(drawer)
            }
            drawer.clear(ColorRGBa.BLACK)
            drawer.shadeStyle = sphere
            drawer.fill = ColorRGBa.WHITE
            drawer.stroke = null
            drawer.circle(width / 2.0, height / 2.0, 200.0)
        }
    }
}
