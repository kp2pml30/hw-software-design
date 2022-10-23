import java.nio.ByteBuffer
import java.util.*
import kotlin.system.exitProcess

object Main {
    private fun help(): Nothing {
        System.err.println("Usage: awt|jfx vector|matrix")
        exitProcess(1)
    }

    private const val USE_VEC = 0
    private const val USE_MAT = 1
    private const val USE_AWT = 0
    private const val USE_JFX = 1

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size != 2) {
            help()
        }

        lateinit var mode: Graph


        val visualizer = when (args[0]) {
            "awt" -> AwtDrawApi()
            "jfx" -> JavaFxApi()
            else -> help()
        }
        mode = when (args[1]) {
            "vector" -> VecGraph(visualizer, listOf(
                    listOf(1, 2),
                    listOf(3),
                    listOf(4, 5),
                    listOf(1),
                    listOf(2),
            ))
            "matrix" -> MatGraph(visualizer, listOf(
                    BitSet.valueOf(arrayOf<Byte>(0b01101).toByteArray()),
                    BitSet.valueOf(arrayOf<Byte>(0b10100).toByteArray()),
                    BitSet.valueOf(arrayOf<Byte>(0b11000).toByteArray()),
                    BitSet.valueOf(arrayOf<Byte>(0b00001).toByteArray()),
                    BitSet.valueOf(arrayOf<Byte>(0b10010).toByteArray()),
            ))
            else -> help()
        }

        visualizer.start() {
            mode.drawGraph()
        }
    }
}