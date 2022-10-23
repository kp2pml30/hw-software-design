interface GenericDrawingApi: DrawingApi {
    fun start(cb: () -> Unit)
}