import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class OmokClient extends Application {

    private static final int SIZE = 19;
    private static final int CELL_SIZE = 30;
    private static final int PADDING = 20;
    private static final int BOARD_SIZE = (SIZE - 1) * CELL_SIZE;

    private Canvas canvas;
    private GraphicsContext gc;
    private Dol[][] board = new Dol[SIZE][SIZE];

    private String nickname;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean myTurn = false;

    private TextArea chatArea;
    private TextField inputField;

    @Override
    public void start(Stage primaryStage) {
        nickname = inputNickname();
        setupNetwork();

        // 바둑판
        canvas = new Canvas(BOARD_SIZE + PADDING * 2, BOARD_SIZE + PADDING * 2);
        gc = canvas.getGraphicsContext2D();
        drawBoard();

        canvas.setOnMouseClicked(this::handleMouseClick);

        // 버튼
        Button generalDolBtn = new Button("장군돌 두기");
        Button attackDolBtn = new Button("공격돌 두기");

        // 채팅창
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefRowCount(5);

        // 입력창
        inputField = new TextField();
        inputField.setPromptText("채팅 입력...");
        inputField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String msg = inputField.getText();
                if (!msg.trim().isEmpty()) {
                    out.println("MSG|" + nickname + "|" + msg);
                    inputField.clear();
                }
            }
        });

        // 오른쪽 UI 패널: 버튼 + 채팅
        VBox rightPanel = new VBox(10);
        rightPanel.getChildren().addAll(generalDolBtn, attackDolBtn, chatArea, inputField);

        // 전체 레이아웃: 왼쪽 바둑판 + 오른쪽 UI
        HBox root = new HBox(20);
        root.getChildren().addAll(canvas, rightPanel);

        // 씬 설정
        Scene scene = new Scene(root, 900, 650);
        primaryStage.setTitle("온라인 오목 - " + nickname);
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(this::receiveLoop).start();
    }

    private void drawBoard() {
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.BLACK);
        for (int i = 0; i < SIZE; i++) {
            gc.strokeLine(PADDING, PADDING + i * CELL_SIZE, PADDING + (SIZE - 1) * CELL_SIZE, PADDING + i * CELL_SIZE);
            gc.strokeLine(PADDING + i * CELL_SIZE, PADDING, PADDING + i * CELL_SIZE, PADDING + (SIZE - 1) * CELL_SIZE);
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null) {
                    board[i][j].draw(gc, j, i);
                }
            }
        }
    }

    private void handleMouseClick(MouseEvent e) {
        if (!myTurn) return;

        int col = (int) ((e.getX() - PADDING + CELL_SIZE / 2) / CELL_SIZE);
        int row = (int) ((e.getY() - PADDING + CELL_SIZE / 2) / CELL_SIZE);

        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) return;
        if (board[row][col] != null) return;

        out.println("MOVE|" + nickname + "|" + row + "|" + col);
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
                if (msg.startsWith("MOVE|")) {
                    String[] parts = msg.split("\\|");
                    String sender = parts[1];
                    int row = Integer.parseInt(parts[2]);
                    int col = Integer.parseInt(parts[3]);

                    Dol dol = sender.equals(nickname) ? new BlackDol() : new WhiteDol();
                    board[row][col] = dol;
                    myTurn = !sender.equals(nickname);
                    Platform.runLater(this::drawBoard);
                } else if (msg.startsWith("MSG|")) {
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

    private String inputNickname() {
        System.out.print("닉네임 입력: ");
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        try {
            return r.readLine();
        } catch (IOException e) {
            return "Guest";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
