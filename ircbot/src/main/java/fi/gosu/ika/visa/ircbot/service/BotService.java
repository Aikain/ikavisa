package fi.gosu.ika.visa.ircbot.service;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
    private final String PROPERTY_NAME_IDENTSERVER_ADDRESS = "identServer.address";
    private final String PROPERTY_NAME_IDENTSERVER_PORT = "identServer.port";

    @Resource
    private Environment env;

    @Autowired
    private UserService userService;
    @Autowired
    private ConfigService configService;
    private Bot bot;

    @PostConstruct
    public void start() {
        bot = new Bot(configService.getConfig(), userService);
        startIdentServer();
        while (!isIdentServerRunning()) {
        }
        bot.connect();
    }

    private boolean isIdentServerRunning() {
        Socket s = null;
        boolean run = false;
        try {
            s = new Socket();
            s.setReuseAddress(true);
            SocketAddress sa = new InetSocketAddress(env.getRequiredProperty(PROPERTY_NAME_IDENTSERVER_ADDRESS), env.getRequiredProperty(PROPERTY_NAME_IDENTSERVER_PORT, Integer.class));
            s.connect(sa, 1000);
        } catch (IOException e) {
        } finally {
            if (s != null) {
                if ( s.isConnected()) run = true;
                try {
                    s.close();
                } catch (IOException e) {
                }
            }
        }
        return run;
    }

    private void startIdentServer() {
        new Thread() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(env.getRequiredProperty(PROPERTY_NAME_IDENTSERVER_PORT, Integer.class));
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
            }
        }.start();
    }
}
