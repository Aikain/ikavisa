package fi.gosu.ika.visa.ircbot.game;

import fi.gosu.ika.visa.ircbot.bot.Bot;
import fi.gosu.ika.visa.ircbot.bot.Game;
import fi.gosu.ika.visa.ircbot.domain.User;

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
    public void start(Bot bot, String channel, User user, String[] args) {
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
    public void message(String channel, User user, String message) {
        if (message.equals(target)) {
            if (!lastSender.equals(user.getSender()) && ++counter >= 6){
                bot.sendMessage(this.channel, "Onneksi olkoon " + user.getSender() + "! Voitit pelin!");
                reset();
                target = user.getSender() + " on paras";
                bot.sendMessage(this.channel, "Aloitetaan uusi peli, uusi tavoite: '" + target + "'");
            } else {
                lastSender = user.getSender();
            }
        }
    }

    @Override
    public void command(String channel, User user, String command, String[] args) {
        switch (command) {
            case "apua":
            case "help":
                help();
        }
    }
}
