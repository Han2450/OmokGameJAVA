import javafx.scene.canvas.GraphicsContext;

abstract class Dol {
    protected int col;
    protected int row;

    public Dol(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public abstract void draw(GraphicsContext gc, int offsetX, int offsetY);
}