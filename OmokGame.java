import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.Optional;

public class OmokGame extends Application {

    private static final int SIZE = 19;
    public static final int CELL_SIZE = 30;
    private Dol[][] board = new Dol[SIZE][SIZE];
    public boolean blackTurn = true;
    public boolean isGameOver = false;
    public boolean samsam = false;
    private boolean useGeneralDol = false;
    private boolean useAttackDol = false;
    private Button generalDolButton;
    private Button attackDolButton;
    private static final int BOARD_SIZE = (SIZE - 1) * CELL_SIZE;
    private static final int PADDING = 20;
    private static final int CANVAS_WIDTH = BOARD_SIZE + PADDING * 2;
    private static final int CANVAS_HEIGHT = BOARD_SIZE + PADDING * 2;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String nickname = "Guest";
    private TextArea chatArea;
    private TextField inputField;

    @Override
    public void start(Stage primaryStage) {
        TextInputDialog dialog = new TextInputDialog("Player");
        dialog.setTitle("닉네임 입력");
        dialog.setHeaderText(null);
        dialog.setContentText("닉네임을 입력하세요:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> nickname = name.trim().isEmpty() ? "Guest" : name);

        setupNetwork();

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawBoard(gc);

        generalDolButton = new Button("장군돌 두기");
        attackDolButton = new Button("공격돌 두기");

        generalDolButton.setLayoutX(5);
        generalDolButton.setLayoutY(20);
        generalDolButton.setPrefWidth(120);
        generalDolButton.setPrefHeight(30);

        attackDolButton.setLayoutX(140);
        attackDolButton.setLayoutY(20);
        attackDolButton.setPrefWidth(120);
        attackDolButton.setPrefHeight(30);


        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefHeight(100);
        chatArea.setPrefWidth(260);
        chatArea.setLayoutX(5);
        chatArea.setLayoutY(80);

        inputField = new TextField();
        inputField.setPromptText("채팅 입력...");
        inputField.setPrefWidth(260);
        inputField.setLayoutX(5);
        inputField.setLayoutY(160);
        inputField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String msg = inputField.getText();
                if (!msg.trim().isEmpty()) {
                    out.println("MSG|" + nickname + "|" + msg);
                    inputField.clear();
                }
            }
        });

        Pane rightPanel = new Pane();
        rightPanel.setPrefSize(300, CANVAS_HEIGHT);
        rightPanel.getChildren().addAll(generalDolButton, attackDolButton, chatArea, inputField);

        HBox root = new HBox(20);
        root.getChildren().addAll(canvas, rightPanel);

        Scene scene = new Scene(root, CANVAS_WIDTH + 300, CANVAS_HEIGHT);
        primaryStage.setTitle("오목 게임 - " + nickname);
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
                            useGeneralDol = false;
                        } else if (useAttackDol) {
                            temp = new AttackDol(col, row);
                            useAttackDol = false;
                        } else {
                            temp = new BlackDol(col, row);
                        }
                        board[row][col] = temp;

                        Rule rule = new Rule(board, SIZE, this);
                        if (rule.SamSam(row, col)) {
                            board[row][col] = null;
                            Alert alert = new Alert(Alert.AlertType.WARNING);
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
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("게임 종료");
                        alert.setHeaderText(null);
                        alert.setContentText((!blackTurn ? "흑돌" : "백돌") + " 승리!");
                        alert.showAndWait();

                        resetGame(gc);
                    }
                }
            }
        });

        new Thread(this::receiveLoop).start();
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

    private void setupNetwork() {
        try {
            socket = new Socket("127.0.0.1", 9999);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("HELLO|" + nickname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveLoop() {
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                if (msg.startsWith("MSG|")) {
                    String[] parts = msg.split("\\|", 3);
                    String sender = parts[1];
                    String content = parts[2];
                    Platform.runLater(() -> chatArea.appendText(sender + ": " + content + "\n"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
