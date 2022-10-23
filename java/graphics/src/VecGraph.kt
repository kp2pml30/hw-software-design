import java.util.*

class VecGraph(drawingApi: DrawingApi?, graph: List<List<Int>>) : Graph(drawingApi) {
    private val graph: List<List<Int>>

    init {
        this.graph = graph
    }

    override fun drawGraph() {
        val n = graph.size
        val angle = 2 * Math.PI / n
        val w = drawingApi.drawingAreaWidth
        val h = drawingApi.drawingAreaHeight
        val getPoint = { i: Int ->
            Pair(Math.sin(i * angle) * w * 0.3 + w/2, Math.cos(i * angle) * h * 0.3 + h/2)
        }
        val angleToInt = { a: Double ->
            a.toInt()
        }
        for (i in graph.indices) {
            val ip = getPoint(i)
            for (j in graph[i]) {
                val jp = getPoint(j)
                drawingApi.drawLine(angleToInt(ip.first), angleToInt(ip.second),
                        angleToInt(jp.first), angleToInt(jp.second))
            }
        }
        for (i in graph.indices) {
            val ip = getPoint(i)
            drawingApi.drawCircle(angleToInt(ip.first), angleToInt(ip.second), 10)
        }
    }
}