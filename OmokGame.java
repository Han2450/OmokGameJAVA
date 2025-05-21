import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class OmokGame extends Application {

    private static final int SIZE = 19;
    public static final int CELL_SIZE = 30;
    private Dol[][] board = new Dol[SIZE][SIZE];
    public boolean blackTurn = true;
    public boolean isGameOver = false;
    public boolean samsam = false;
    private boolean useGeneralDol = false;
    private boolean useNormalDol = false;
    private boolean useAttackDol = false;
    private Button generalDolButton;
    private Button normalDolButton;
    private Button attackDolButton;
    private static final int BOARD_SIZE = (SIZE - 1) * CELL_SIZE;
    private static final int PADDING = 20;
    private static final int CANVAS_WIDTH = BOARD_SIZE + PADDING * 2;
    private static final int CANVAS_HEIGHT = BOARD_SIZE + PADDING * 2;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawBoard(gc);

        generalDolButton = new Button("장군돌 두기");
        attackDolButton = new Button("공격돌 두기");

        generalDolButton.setLayoutX(600);
        generalDolButton.setLayoutY(20);
        attackDolButton.setLayoutX(800);
        attackDolButton.setLayoutY(20);

        generalDolButton.setOnAction(e -> {
            useGeneralDol = true;
            useAttackDol = false;
        });
        
        attackDolButton.setOnAction(e -> {
            useGeneralDol = false;
            useAttackDol = true;
        });

        Pane root = new Pane(canvas, generalDolButton, attackDolButton);

        Scene scene = new Scene(root);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(605);
        primaryStage.setTitle("오목 게임");
        primaryStage.setScene(scene);
        primaryStage.show();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (isGameOver) return;

            int offsetX = (CANVAS_WIDTH - BOARD_SIZE) / 2;
            int offsetY = (CANVAS_HEIGHT - BOARD_SIZE) / 2;

            int col = (int) Math.round((e.getX() - offsetX) / CELL_SIZE);
            int row = (int) Math.round((e.getY() - offsetY) / CELL_SIZE);

            if (col >= 0 && col < SIZE && row >= 0 && row < SIZE) {
                if (board[row][col] == null) {

                    if (blackTurn) {
                        Dol temp;
                        if (useGeneralDol) {
                            temp = new GeneralDol(col, row);
                            useGeneralDol = false; // 한 번 사용 후 해제
                        } else if (useAttackDol) {
                            temp = new AttackDol(col, row);
                            useAttackDol = false; // 한 번 사용 후 해제
                        } else {
                            temp = new BlackDol(col, row);
                        }
                        board[row][col] = temp;

                        Rule rule = new Rule(board, SIZE, this);
                        if (rule.SamSam(row, col)) {
                            board[row][col] = null;
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("금수");
                            alert.setHeaderText(null);
                            alert.setContentText("쌍삼 금수입니다. 다른 자리에 놓아주세요.");
                            alert.showAndWait();
                            return;
                        }
                    } else {
                        board[row][col] = new WhiteDol(col, row);
                    }

                    blackTurn = !blackTurn;
                    drawBoard(gc);

                    WinChecker checker = new WinChecker(board, SIZE, this);
                    if (checker.checkWin(row, col)) {
                        isGameOver = true;
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("게임 종료");
                        alert.setHeaderText(null);
                        alert.setContentText((!blackTurn ? "흑돌" : "백돌") + " 승리!");
                        alert.showAndWait();

                        resetGame(gc);
                    }
                }
            }
        });
    }

    private void drawBoard(GraphicsContext gc) {
        gc.setFill(Color.rgb(238, 207, 161));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        int offsetX = (CANVAS_WIDTH - BOARD_SIZE) / 2;
        int offsetY = (CANVAS_HEIGHT - BOARD_SIZE) / 2;

        gc.setStroke(Color.BLACK);
        for (int i = 0; i < SIZE; i++) {
            gc.strokeLine(offsetX + i * CELL_SIZE, offsetY, offsetX + i * CELL_SIZE, offsetY + BOARD_SIZE);
            gc.strokeLine(offsetX, offsetY + i * CELL_SIZE, offsetX + BOARD_SIZE, offsetY + i * CELL_SIZE);
        }

        int[] points = {3, 9, 15};
        gc.setFill(Color.BLACK);
        for (int row : points) {
            for (int col : points) {
                gc.fillOval(offsetX + col * CELL_SIZE - 4, offsetY + row * CELL_SIZE - 4, 8, 8);
            }
        }

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != null) {
                    board[row][col].draw(gc, offsetX, offsetY);
                }
            }
        }
    }

    private void resetGame(GraphicsContext gc) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = null;
            }
        }
        blackTurn = true;
        isGameOver = false;
        drawBoard(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
