import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.stage.Stage


/**
 * @author akirakozov
 */
class JavaFxApi : Application(), GenericDrawingApi {
    private val root = Group()
    private val canvas = Canvas(600.0, 400.0)
    private val gc: GraphicsContext = canvas.graphicsContext2D

    init {
        gc.fill = Color.GREEN
        gc.stroke = Color.BLACK
    }

    override fun start(primaryStage: Stage) {
        println("initt")
        primaryStage.title = "Drawing circle"

        root.children.add(canvas)
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

    override fun getDrawingAreaWidth(): Long {
        return canvas.width.toLong()
    }

    override fun getDrawingAreaHeight(): Long {
        return canvas.height.toLong()
    }

    override fun drawCircle(x: Int, y: Int, r: Int) {
        val x = x.toDouble()
        val y = y.toDouble()
        val r = r.toDouble()
        gc.fillOval(x - r/2, y - r/2, r, r)
    }

    override fun drawLine(x: Int, y: Int, x1: Int, y1: Int) {
        gc.strokeLine(x.toDouble(), y.toDouble(), x1.toDouble(), y1.toDouble())
    }

    override fun start(cb: () -> Unit) {
        cb()

        this.init()

        Platform.startup {
            val stage = Stage()
            this.start(stage)
        }
    }
}