package fi.gosu.ika.visa.ircbot.service;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Aikain on 15.10.2016.
 */
@Service
public class BotService {
    @Value("${bot.identServer.address}")
    private String identServerAddress;
    @Value("${bot.identServer.port}")
    private String identServerPort;

    private final UserService userService;
    private final ConfigService configService;
    private final PointService pointService;
    private final QuestionService questionService;

    private Bot bot;

    public BotService(UserService userService, ConfigService configService, PointService pointService, QuestionService questionService) {
        this.userService = userService;
        this.configService = configService;
        this.pointService = pointService;
        this.questionService = questionService;
    }

    @PostConstruct
    public void start() {
        bot = new Bot(configService.getConfig(), userService, pointService, questionService);
        if (identServerAddress != null && !identServerAddress.isEmpty() && identServerPort != null && !identServerPort.isEmpty()) {
            startIdentServer();
            while (!isIdentServerRunning()) {
            }
        }
        bot.connect();
    }

    private boolean isIdentServerRunning() {
        Socket s = null;
        boolean run = false;
        try {
            s = new Socket();
            s.setReuseAddress(true);
            SocketAddress sa = new InetSocketAddress(identServerAddress, Integer.parseInt(identServerPort));
            s.connect(sa, 1000);
        } catch (IOException ignored) {
        } finally {
            if (s != null) {
                if (s.isConnected()) run = true;
                try {
                    s.close();
                } catch (IOException ignored) {
                }
            }
        }
        return run;
    }

    private void startIdentServer() {
        new Thread(() -> {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(Integer.parseInt(identServerPort));
                serverSocket.setSoTimeout('\uea60');
                while (true) {
                    Socket socket = serverSocket.accept();
                    BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String textIn = socketIn.readLine();
                    if (textIn != null && !textIn.isEmpty()) {
                        socketOut.write(textIn + " : USERID : UNIX : " + bot.getLogin() + "\r\n");
                        socketOut.flush();
                        socketOut.close();
                        break;
                    }
                }
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
