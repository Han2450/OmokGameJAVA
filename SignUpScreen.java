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

public class SignUpScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        // UI 구성
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // 사용자명, 비밀번호, 이메일 입력 필드
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        // 회원가입 버튼
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(_ -> handleSignUp(usernameField.getText(), passwordField.getText(), emailField.getText()));

        // UI 구성 요소 추가
        grid.add(usernameField, 0, 0);
        grid.add(passwordField, 0, 1);
        grid.add(emailField, 0, 2);
        grid.add(signUpButton, 0, 3);

        Scene scene = new Scene(grid, 300, 250);
        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // // 회원가입 처리 메서드
    // private void handleSignUp(String username, String password, String email) {
    //     // MySQL에 사용자 추가
    //     if (DatabaseConnector.addUser(username, password, email)) {
    //         // 성공하면 알림창 띄우기
    //         showAlert(AlertType.INFORMATION, "Sign Up Successful", "Welcome, " + username + "!");
    //     } else {
    //         // 실패하면 알림창 띄우기
    //         showAlert(AlertType.ERROR, "Sign Up Failed", "An error occurred while signing up.");
    //     }
    // }
    private void handleSignUp(String username, String password, String email) {
        // MySQL에 사용자 추가
        boolean isSuccess = DatabaseConnector.addUser(username, password, email);
        if (isSuccess) {
            showAlert(AlertType.INFORMATION, "Sign Up Successful", "Welcome, " + username + "!");
        } else {
            showAlert(AlertType.ERROR, "Sign Up Failed", "An error occurred while signing up.");
        }
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
