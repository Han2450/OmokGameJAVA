import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginScreen extends Application {

    @Override
    public void start(Stage primaryStage) {

        // UI 구성
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // 사용자명과 비밀번호 입력 필드
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // 로그인 버튼
        Button loginButton = new Button("Login");
        loginButton.setOnAction(_ -> handleLogin(usernameField.getText(), passwordField.getText()));

        // 회원가입 버튼
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(_ -> handleSignUp(primaryStage));

        // UI 구성 요소 추가
        grid.add(usernameField, 0, 0);
        grid.add(passwordField, 0, 1);
        grid.add(loginButton, 0, 2);
        grid.add(signUpButton, 0, 3);  // 회원가입 버튼 추가

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 로그인 처리 메서드
    private void handleLogin(String username, String password) {
        if (DatabaseConnector.checkUser(username, password)) {
            // 로그인 성공
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome " + username);
        } else {
            // 로그인 실패
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    // 회원가입 화면 띄우기
    private void handleSignUp(Stage primaryStage) {
        // 새로운 창 띄우기
        SignUpScreen signUpScreen = new SignUpScreen();  // SignUpScreen 클래스는 따로 만들어야 함
        signUpScreen.start(new Stage());  // 새 Stage로 회원가입 창 띄우기
        primaryStage.close();  // 로그인 창은 닫기
    }

    // 알림창을 띄우는 메서드
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
