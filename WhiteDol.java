import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class WhiteDol extends Dol {
    public WhiteDol(int col, int row) {
        super(col, row);
    }

    @Override
    public void draw(GraphicsContext gc, int offsetX, int offsetY) {
        gc.setFill(Color.WHITE);
        gc.fillOval(offsetX + col * OmokGame.CELL_SIZE - (OmokGame.CELL_SIZE / 2) + 5,
                    offsetY + row * OmokGame.CELL_SIZE - (OmokGame.CELL_SIZE / 2) + 5,
                    OmokGame.CELL_SIZE - 10, OmokGame.CELL_SIZE - 10);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(offsetX + col * OmokGame.CELL_SIZE - (OmokGame.CELL_SIZE / 2) + 5,
                      offsetY + row * OmokGame.CELL_SIZE - (OmokGame.CELL_SIZE / 2) + 5,
                      OmokGame.CELL_SIZE - 10, OmokGame.CELL_SIZE - 10);
    }
}