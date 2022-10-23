import java.awt.Color
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D

class AwtDrawApi : Frame(), GenericDrawingApi {
    var ga: Graphics2D? = null

    private var cb: () -> Unit = {}

    override fun paint(g: Graphics) {
        val ga = g as Graphics2D
        this.ga = ga
        ga.paint = Color.green

        cb()
    }

    override fun getDrawingAreaWidth(): Long {
        return 600
    }

    override fun getDrawingAreaHeight(): Long {
        return 400
    }

    override fun drawCircle(x: Int, y: Int, r: Int) {
        val x = x.toFloat()
        val y = y.toFloat()
        val r = r.toFloat()
        ga!!.fill(Ellipse2D.Float(x-r/2, y-r/2, r, r))
    }
    override fun drawLine(x: Int, y: Int, x1: Int, y1: Int) {
        ga!!.drawLine(x, y, x1, y1)
    }
    override fun start(cb: () -> Unit) {
        this.cb = cb
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent) {
                System.exit(0)
            }
        })
        setSize(600, 400)
        isVisible = true
    }
}