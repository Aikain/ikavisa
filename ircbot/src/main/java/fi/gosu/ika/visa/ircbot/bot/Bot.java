package fi.gosu.ika.visa.ircbot.bot;

import fi.gosu.ika.visa.ircbot.domain.User;
import fi.gosu.ika.visa.ircbot.service.PointService;
import fi.gosu.ika.visa.ircbot.service.QuestionService;
import fi.gosu.ika.visa.ircbot.service.UserService;
import fi.gosu.ika.visa.ircbot.tools.Config;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aikain on 13.10.2016.
 */
public class Bot extends PircBot {

    private MessageHandler messageHandler;
    private Game game;
    private List<String> channels;
    private Config config;
    private UserService userService;
    private PointService pointService;
    private QuestionService questionService;


    public Bot(Config config, UserService userService, PointService pointService, QuestionService questionService) {
        this.setName(config.getName());
        this.setLogin(config.getLogin());
        this.setVerbose(config.getDebug());
        this.config = config;
        this.userService = userService;
        this.pointService = pointService;
        this.questionService = questionService;
        this.messageHandler = new MessageHandler(this);
        this.channels = new ArrayList<>();
        channels.addAll(config.getChannels());
    }

    public void connect() {
        try {
            this.connect(config.getServer());
            this.sendRawLine(config.getLoginCmd().replace("$loginName", config.getLoginName()).replace("$loginPass", config.getLoginPass()));
            channels.forEach(this::joinChannel);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void join(String channel) {
        this.joinChannel(channel);
        this.channels.add(channel);
    }

    public void leave(String channel, String sender) {
        this.partChannel(channel, sender + " doesn't want to see me there anymore ='(");
        this.channels.remove(channel);
    }

    public void restart() {
        try {
            Process p = Runtime.getRuntime().exec("mvn exec:java");
            int exitCode = p.waitFor();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            System.out.println("Finished with code: " + String.valueOf(exitCode));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void quit() {
        this.quitServer();
        System.exit(0);
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        messageHandler.onMessage(channel, userService.getUser(hostname, sender, login), message);
    }

    @Override
    protected void onDisconnect() {
        try {
            this.reconnect();
            channels.forEach(this::joinChannel);
        } catch (Exception e) {
            try {
                Thread.sleep(15000);
                onDisconnect();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    public boolean startGame(String channel, User user, String[] args) {
        if (args.length == 0) {
            if (game == null) return false;
        } else {
            try {
                Class gameClass = Class.forName("fi.gosu.ika.visa.ircbot.game." + (args[0].charAt(0) + "").toUpperCase() + (args[0].length() > 1 ? args[0].substring(1) : ""));
                if (game == null || game.getClass() != gameClass) {
                    game = (Game) gameClass.newInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        game.start(this, channel, user, args);
        return true;
    }

    public void stopGame() {
        if (game != null) {
            game.stop();
        }
    }

    public Game getGame() {
        return game;
    }

    public void clearGame() {
        this.game = null;
    }

    public UserService getUserService() { return userService; }
    public PointService getPointService() { return pointService; }
    public QuestionService getQuestionService() { return questionService; }
}
