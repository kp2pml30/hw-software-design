public interface DrawingApi {
    long getDrawingAreaWidth();
    long getDrawingAreaHeight();
    void drawCircle(int x, int y, int r);
    void drawLine(int x, int y, int x1, int y1);
}
