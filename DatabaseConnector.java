import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnector {

    // // MySQL 연결 메서드
    // public static Connection connect() {
    //     try {
    //         // MySQL JDBC 드라이버 로드
    //         Class.forName("com.mysql.cj.jdbc.Driver");  // MySQL 드라이버 로드

    //         // MySQL 데이터베이스 URL, 사용자명, 비밀번호
    //         String url = "jdbc:mysql://127.0.0.1:3306/omok_game";  // DB URL
    //         String user = "root";  // MySQL 사용자명
    //         String password = "rlacks-1129";  // MySQL 비밀번호

    //         // 데이터베이스 연결
    //         return DriverManager.getConnection(url, user, password);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }
    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
            String url = "jdbc:mysql://127.0.0.1:3306/omok?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            String user = "root";  // MySQL 사용자명
            String password = "rlacks-1129";  // MySQL 비밀번호
    
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("MySQL 연결 성공!");  // 연결 성공 로그
            }
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("MySQL 연결 실패!");
        }
        return null;  // 연결 실패
    }
    

    // // 사용자 추가 (회원가입)
    // public static boolean addUser(String username, String password, String email) {
    //     try (Connection conn = connect()) {
    //         if (conn != null) {
    //             // 사용자 정보 INSERT 쿼리
    //             String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    //             PreparedStatement stmt = conn.prepareStatement(query);
    //             stmt.setString(1, username);  // 사용자명
    //             stmt.setString(2, password);  // 비밀번호
    //             stmt.setString(3, email);     // 이메일

    //             int rowsAffected = stmt.executeUpdate();
    //             return rowsAffected > 0;  // 삽입된 행이 있으면 성공
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return false;  // 실패
    // }
    public static boolean addUser(String username, String password, String email) {
        try (Connection conn = connect()) {
            if (conn != null) {
                String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);  // 사용자명
                stmt.setString(2, password);  // 비밀번호
                stmt.setString(3, email);     // 이메일
    
                int rowsAffected = stmt.executeUpdate();  // 데이터베이스에 추가
                if (rowsAffected > 0) {
                    System.out.println("회원가입 성공");
                    return true;
                } else {
                    System.out.println("회원가입 실패");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //지워도됨(디버깅)
            System.out.println("회원가입 오류 발생: " + e.getMessage());
        }
        return false;  // 실패
    }
    

    // 사용자 로그인 검증
    public static boolean checkUser(String username, String password) {
        try (Connection conn = connect()) {
            if (conn != null) {
                // 사용자명과 비밀번호로 조회하는 쿼리
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);   // 사용자명
                stmt.setString(2, password);   // 비밀번호

                ResultSet rs = stmt.executeQuery();
                return rs.next();  // 결과가 있으면 로그인 성공
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;  // 로그인 실패
    }
}
