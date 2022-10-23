import java.util.function.Consumer;

public abstract class Graph {
    protected final DrawingApi drawingApi;

    public Graph(final DrawingApi drawingApi) {
        this.drawingApi = drawingApi;
    }

    public abstract void drawGraph();
}
