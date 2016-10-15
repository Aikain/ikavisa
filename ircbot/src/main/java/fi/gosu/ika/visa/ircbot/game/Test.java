package fi.gosu.ika.visa.ircbot.game;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import fi.gosu.ika.visa.ircbot.bot.Game;

/**
 * Created by Aikain on 14.10.2016.
 */
public class Test implements Game {
    private boolean run;
    private Bot bot;
    private String channel;
    private int counter;
    private String lastSender;
    private String target;

    @Override
    public void start(Bot bot, String channel, String sender, String login, String hostname, String[] args) {
        this.bot = bot;
        this.channel = channel;
        this.run = true;
        reset();
    }

    @Override
    public void stop() {
        this.run = false;
    }

    @Override
    public void reset() {
        this.counter = 0;
        this.lastSender = "";
        this.target = "Tomiz on paras";
    }

    @Override
    public void simpleHelp() {
        bot.sendMessage(channel, "Tervetuloa pelaamaan test-peliä!");
        bot.sendMessage(channel, "Pelin tavoitteena on olla kuudes, joka sanoo '" + target + "'.");
        bot.sendMessage(channel, "Peräkkäin ei saa sanoa.");
    }

    @Override
    public void help() {
        bot.sendMessage(channel, "Pelin tavoitteena on olla kuudes, joka sanoo '" + target + "'.");
        bot.sendMessage(channel, "Peräkkäin ei saa sanoa.");
    }

    @Override
    public void message(String channel, String sender, String login, String hostname, String message) {
        if (message.equals(target)) {
            if (++counter >= 6 && !lastSender.equals(sender)){
                bot.sendMessage(this.channel, "Onneksi olkoon " + sender + "! Voitit pelin!");
                reset();
                target = sender + " on paras";
                bot.sendMessage(this.channel, "Aloitetaan uusi peli, uusi tavoite: '" + target + "'");
            } else {
                lastSender = sender;
            }
        }
    }

    @Override
    public void command(String channel, String sender, String login, String hostname, String command, String[] args) {
        switch (command) {
            case "apua":
            case "help":
                help();
        }
    }
}
