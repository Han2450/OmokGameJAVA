import java.io.*;
import java.net.*;
import java.util.*;

public class OmokServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();

    public OmokServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("서버 시작됨. 포트: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                handler.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message) {
        for (ClientHandler ch : clients) {
            ch.sendMessage(message);
        }
    }

    class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname = "Unknown";

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String msg) {
            out.println(msg);
        }

        @Override
        public void run() {
            String msg;
            try {
                while ((msg = in.readLine()) != null) {
                    if (msg.startsWith("HELLO|")) {
                        nickname = msg.substring(6); // 닉네임 저장
                        System.out.println("닉네임 등록: " + nickname);
                    } else if (msg.startsWith("MSG|")) {
                        String[] parts = msg.split("\\|", 3);
                        if (parts.length == 3) {
                            String sender = parts[1];
                            String content = parts[2];
                            System.out.println(sender + ": " + content);
                            broadcast("MSG|" + sender + "|" + content);
                        }
                    } else if (msg.startsWith("MOVE|")) {
                        // ✅ 수신된 수 출력 및 전체에게 전달
                        System.out.println("수신된 수: " + msg);
                        broadcast(msg);
                    }
                }
            } catch (IOException e) {
                System.out.println("클라이언트 연결 끊김: " + nickname);
            }
        }
    }

    public static void main(String[] args) {
        new OmokServer(9999);
    }
}
