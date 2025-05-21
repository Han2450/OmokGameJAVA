import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WhiteGeneralDol extends Dol {
    public WhiteGeneralDol(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(GraphicsContext gc, int offsetX, int offsetY) {
        gc.setFill(Color.RED); // 장군돌은 빨간 돌
        gc.fillOval(offsetX + col * OmokGame.CELL_SIZE - 12, offsetY + row * OmokGame.CELL_SIZE - 12, 24, 24);
        gc.setStroke(Color.WHITE);
        gc.strokeOval(offsetX + col * OmokGame.CELL_SIZE - 12, offsetY + row * OmokGame.CELL_SIZE - 12, 24, 24);
    }
}
