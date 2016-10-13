package fi.gosu.ika.visa.ircbot;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;

/**
 * Created by Aikain on 13.10.2016.
 */
public class Bot extends PircBot {

    private MessageHandler messageHandler;

    public Bot(String name, String login) {
        this.setName(name);
        this.setLogin(login);
        this.setVerbose(true);
        this.messageHandler = new MessageHandler(this);
    }

    public void connect() {
        try {
            this.connect("irc.OnlineGamesNet.net");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IrcException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void quit() {
        this.quitServer();
        System.exit(0);
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        messageHandler.onMessage(channel, sender, login, hostname, message);
    }
}
